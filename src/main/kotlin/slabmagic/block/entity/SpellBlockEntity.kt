package slabmagic.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.math.BlockPos
import slabmagic.spell.build.AssembledSpell


open class SpellBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BlockEntity(type, pos, state){

    var spell: AssembledSpell? = null

    override fun writeNbt(nbt: NbtCompound) {
        spell ?.let {
            nbt.put("spell", it.toNbt())
        }
    }

    override fun readNbt(nbt: NbtCompound) {
        nbt.getList("spell",NbtElement.STRING_TYPE.toInt()) ?.let {
            spell = AssembledSpell.fromNbt(it)
        }
    }

    override fun toUpdatePacket() = BlockEntityUpdateS2CPacket.create(this)

    override fun toInitialChunkDataNbt() = createNbt()

    companion object{
        fun factory(pos: BlockPos, state: BlockState)
            = SpellBlockEntity(SlabMagicBlockEntities.SPELL, pos, state)
    }
}