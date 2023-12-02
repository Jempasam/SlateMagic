package slabmagic.block.entity

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.Packet
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.*
import net.minecraft.world.World
import slabmagic.helper.ColorTools
import slabmagic.particle.EnergyBlockParticleEffect
import slabmagic.particle.SlabMagicParticles
import slabmagic.particle.SpellCircleParticleEffect
import slabmagic.registry.SlabMagicRegistry
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.build.node.visitor.AmbiguousPartNodeException
import slabmagic.spell.build.node.visitor.SpellNodeVisitor
import slabmagic.spell.build.parts.SlabMagicSpellParts
import slabmagic.spell.build.parts.SpellBuildResult
import slabmagic.spell.build.parts.SpellPart
import slabmagic.spell.build.parts.assemble
import slabmagic.spell.build.visitor.AmbiguousPartVisitorException
import slabmagic.spell.build.visitor.NodeVisitor
import slabmagic.spell.build.visitor.Visited
import slabmagic.spell.build.visitor.VisitorException
import slabmagic.spell.effect.SpellEffect


open class NodeBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BlockEntity(type, pos, state){

    var node: SpellPart<*>? = null
        get() = field
        set(value){
            field = value
            val world=world
            if(world!=null && !world.isClient){
                val state=world.getBlockState(pos)
                world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS)
                markDirty()
            }
            val color=field?.color ?: Vec3f.ZERO
        }

    override fun writeNbt(nbt: NbtCompound) {
        node ?.let{SlabMagicRegistry.PARTS.getId(node)}?.let { nbt.putString("node", it.toString()) }
    }

    override fun readNbt(nbt: NbtCompound) {
        nbt.getString("node")
            ?.let{ Identifier.tryParse(it) }
            ?.let { SlabMagicRegistry.PARTS.get(it) }
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

fun fetchNodes(list: MutableList<SpellPart<*>>, world: World, pos: BlockPos, direction: Direction): Pair<BlockPos,Direction>?{
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
                if(finded!=null)throw Exception("Slab has more than one connection")
                finded=fetched
            }
        }
        return finded ?: Pair(pos,direction)
    }
    return null
}

fun fetchNodes(world: World, pos: BlockPos, direction: Direction): Triple<BlockPos,Direction,List<SpellPart<*>>>{
    val list= mutableListOf<SpellPart<*>>()
    val end= fetchNodes(list,world,pos,direction) ?: throw Exception("Invalid Slab Position")
    if(list.isEmpty()) throw Exception("Empty Slab Structure")
    return Triple(end.first,end.second,list)
}

fun fetchSpellPart(world: World, pos: BlockPos, direction: Direction): Triple<BlockPos,Direction,SpellBuildResult<*>>{
    val (epos,edirection,nodes)=fetchNodes(world,pos,direction)
    return Triple(epos,edirection,nodes.assemble())
}


fun <V: SpellNodeVisitor<V>> visitNodes(previous: SpellPart<*>, world: World, pos: BlockPos, direction: Direction, visitor: V): SpellPart<*>?{
    // Performance Tests
    if(world.isAir(pos))return null

    val blockEntity=world.getBlockEntity(pos)
    if(blockEntity is NodeBlockEntity){
        val part=blockEntity.node
        part?.let { visitor.visit(world,pos,direction,it) }
        val previous=part ?: previous
        var finded: SpellPart<*>?=null
        for(next_direction in Direction.entries)if(next_direction!=direction.opposite){
            val next_pos=pos.offset(next_direction)
            val fetched=visitNodes(previous,world,next_pos,next_direction,visitor)
            if(fetched!=null){
                if(finded!=null){
                    val except=AmbiguousPartNodeException(previous,finded,fetched)
                    visitor.exception(world,pos,direction,except)
                    throw except
                }
                finded=fetched
            }
        }
        if(finded==null)visitor.end(world,pos,direction)
        return previous
    }
    return null
}


fun <V: SpellNodeVisitor<V>> visitNodes(world: World, pos: BlockPos, direction: Direction, visitor: V){
    visitNodes(SlabMagicSpellParts.STUB,world,pos,direction,visitor)
}

class NodeBlockVisited(val world: World, val pos: BlockPos, val direction: Direction): Visited{
    override fun error(exception: VisitorException){
        show(ColorTools.RED)
    }

    override fun say(msg: Text) {
        show(ColorTools.LIME)
    }

    override fun cast(effect: SpellEffect, power: Int): SpellContext? {
        if(world is ServerWorld)return  effect.use(
            SpellContext.at(
            world,
            Vec3d.ofCenter(pos.offset(direction)),
            Vec2f(direction.offsetY*-90f,direction.asRotation()),
            power
        ))
        return null
    }

    override fun show(shape: SpellShape, color: Int) {
        val pos=Vec3d.ofCenter(pos)
        if(world is ServerWorld)world.spawnParticles(
            SpellCircleParticleEffect(SlabMagicParticles.SPELL_CROSSED,shape,color,1.0f),
            pos.x, pos.y, pos.z,    1,
            0.0, 0.0, 0.0,          0.0
        )
        else world.addParticle(
            SpellCircleParticleEffect(SlabMagicParticles.SPELL_CROSSED,shape,color,1.0f),
            pos.x, pos.y, pos.z,
            0.0, 0.0, 0.0
        )
    }

    override fun show(color: Vec3f) {
        val pos=Vec3d.ofCenter(pos)
        if(world is ServerWorld)world.spawnParticles(
            EnergyBlockParticleEffect(color,color,0.5f),
            pos.x, pos.y, pos.z,    1,
            0.0, 0.0, 0.0,          0.0
        )
        else world.addParticle(
            EnergyBlockParticleEffect(color,color,0.5f),
            pos.x, pos.y, pos.z,
            0.0, 0.0, 0.0
        )
    }

}

fun NodeVisitor.visitAt(previous: SpellPart<*>, world: World, pos: BlockPos, direction: Direction): SpellPart<*>?{

    // Performance Tests
    if(world.isAir(pos))return null

    val blockEntity=world.getBlockEntity(pos)
    if(blockEntity is NodeBlockEntity){
        val part=blockEntity.node
        print(part?.name?.string)
        val visited=NodeBlockVisited(world,pos,direction)
        part?.let { visit(visited,it) }
        val previous=part ?: previous
        var finded: SpellPart<*>?=null
        for(next_direction in Direction.entries)if(next_direction!=direction.opposite){
            val next_pos=pos.offset(next_direction)
            val fetched=visitAt(previous,world,next_pos,next_direction)
            if(fetched!=null){
                if(finded!=null){
                    val except=AmbiguousPartVisitorException(visited,previous,2)
                    exception(visited,except)
                    throw except
                }
                finded=fetched
            }
        }
        if(finded==null)end(NodeBlockVisited(world,pos,direction))
        return previous
    }
    return null
}

fun NodeVisitor.visitAt(world: World, pos: BlockPos, direction: Direction){
    visitAt(SlabMagicSpellParts.STUB,world,pos,direction)
}