package slabmagic.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos


open class PartBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BlockEntity(type, pos, state){

    //override fun toUpdatePacket() = BlockEntityUpdateS2CPacket.create(this)

    //override fun toInitialChunkDataNbt(registry: RegistryWrapper.WrapperLookup) = createNbt(registry)

    companion object{
        fun factory(pos: BlockPos, state: BlockState) = PartBlockEntity(SlabMagicBlockEntities.SPELL_PART, pos, state)
    }
}