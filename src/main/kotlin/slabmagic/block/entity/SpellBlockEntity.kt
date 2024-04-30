package slabmagic.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.math.BlockPos
import slabmagic.spell.build.AssembledSpell


open class SpellBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BlockEntity(type, pos, state){

    var spell: AssembledSpell? = null


    override fun toUpdatePacket() = BlockEntityUpdateS2CPacket.create(this)

    override fun toInitialChunkDataNbt(registry: RegistryWrapper.WrapperLookup) = createNbt(registry)

    companion object{
        fun factory(pos: BlockPos, state: BlockState)
            = SpellBlockEntity(SlabMagicBlockEntities.SPELL, pos, state)
    }
}