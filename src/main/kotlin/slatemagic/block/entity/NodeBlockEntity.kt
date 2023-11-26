package slatemagic.block.entity

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.Packet
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import slatemagic.registry.SlateMagicRegistry
import slatemagic.spell.build.SpellNode
import slatemagic.spell.build.SpellPart
import slatemagic.spell.build.assemble


open class NodeBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BlockEntity(type, pos, state){

    var node: SpellNode<*>? = null
        get() = field
        set(value){
            field = value
            val world=world
            if(world!=null && !world.isClient){
                val state=world.getBlockState(pos)
                world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS)
                markDirty()
            }
        }

    override fun writeNbt(nbt: NbtCompound) {
        node ?.let{SlateMagicRegistry.NODES.getId(node)}?.let { nbt.putString("node", it.toString()) }
    }

    override fun readNbt(nbt: NbtCompound) {
        nbt.getString("node")
            ?.let{ Identifier.tryParse(it) }
            ?.let { SlateMagicRegistry.NODES.get(it) }
            ?.let { node = it }
        val world=world
        if(world!=null && world.isClient){
            world.updateListeners(pos,world.getBlockState(pos),world.getBlockState(pos),Block.NOTIFY_LISTENERS);
        }
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener> {
        return BlockEntityUpdateS2CPacket.create(this)
    }

    override fun toInitialChunkDataNbt(): NbtCompound {
        return createNbt()
    }
}

fun fetchNodes(list: MutableList<SpellNode<*>>, world: World, pos: BlockPos, direction: Direction): Pair<BlockPos,Direction>?{
    // Performance Tests
    if(world.isAir(pos))return null

    val blockEntity=world.getBlockEntity(pos)
    if(blockEntity is NodeBlockEntity){
        blockEntity.node?.let { list.add(it) }
        var finded: Pair<BlockPos,Direction>?=null
        for(next_direction in Direction.entries)if(next_direction!=direction.opposite){
            val next_pos=pos.offset(next_direction)
            val fetched=fetchNodes(list,world,next_pos,next_direction)
            if(fetched!=null){
                if(finded!=null)throw Exception("Slate has more than one connection")
                finded=fetched
            }
        }
        return finded ?: Pair(pos,direction)
    }
    return null
}

fun fetchNodes(world: World, pos: BlockPos, direction: Direction): Triple<BlockPos,Direction,List<SpellNode<*>>>{
    val list= mutableListOf<SpellNode<*>>()
    val end= fetchNodes(list,world,pos,direction) ?: throw Exception("Invalid Slate Position")
    if(list.isEmpty()) throw Exception("Empty Slate Structure")
    return Triple(end.first,end.second,list)
}

fun fetchSpellPart(world: World, pos: BlockPos, direction: Direction): Triple<BlockPos,Direction,SpellPart<*>>{
    val (epos,edirection,nodes)=fetchNodes(world,pos,direction)
    return Triple(epos,edirection,nodes.assemble())
}