package assets.`slate-magic`.block.color

import net.minecraft.block.BlockState
import net.minecraft.client.color.block.BlockColorProvider
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView
import slatemagic.block.entity.NodeBlockEntity
import slatemagic.helper.ColorTools

object NodeBlockColor: BlockColorProvider {
    override fun getColor(state: BlockState, world: BlockRenderView?, pos: BlockPos?, tintIndex: Int): Int {
        world?.let {
            val entity = world.getBlockEntity(pos)
            if(entity is NodeBlockEntity){
                entity.node?.let {
                    return ColorTools.int(it.color)
                }
            }
        }
        return DyeColor.GRAY.fireworkColor
    }
}