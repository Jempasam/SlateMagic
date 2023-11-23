package slatemagic.block.entity

import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class SlateBlockEntity(pos: BlockPos, state: BlockState): NodeBlockEntity(SlateMagicBlockEntities.SLATE_BLOCK, pos, state)