package slabmagic.block.entity

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3f
import slabmagic.registry.SlabMagicRegistry
import slabmagic.spell.build.parts.SpellPart


open class PartBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BlockEntity(type, pos, state){

    @Suppress("RedundantGetter")
    var part: SpellPart<*>? = null
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
        part ?.let{SlabMagicRegistry.PARTS.getId(part)}?.let { nbt.putString("node", it.toString()) }
    }

    override fun readNbt(nbt: NbtCompound) {
        nbt.getString("node")
            ?.let{ Identifier.tryParse(it) }
            ?.let { SlabMagicRegistry.PARTS.get(it) }
            ?.let { part = it }
        val world=world
        if(world!=null && world.isClient){
            world.updateListeners(pos,world.getBlockState(pos),world.getBlockState(pos),Block.NOTIFY_LISTENERS)
        }
    }



    override fun toUpdatePacket() = BlockEntityUpdateS2CPacket.create(this)

    override fun toInitialChunkDataNbt() = createNbt()

    companion object{
        fun factory(pos: BlockPos, state: BlockState)
            = PartBlockEntity(SlabMagicBlockEntities.SPELL_PART, pos, state)

    }
}