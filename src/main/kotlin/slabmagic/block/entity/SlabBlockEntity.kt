package slabmagic.block.entity

import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class SlabBlockEntity(pos: BlockPos, state: BlockState): NodeBlockEntity(SlabMagicBlockEntities.SLAB, pos, state)