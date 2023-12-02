package assets.`slab-magic`.block.color

import net.minecraft.block.BlockState
import net.minecraft.client.color.block.BlockColorProvider
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView
import slabmagic.block.entity.NodeBlockEntity
import slabmagic.helper.ColorTools
import slabmagic.spell.build.parts.code

object NodeBlockColor: BlockColorProvider {
    override fun getColor(state: BlockState, world: BlockRenderView?, pos: BlockPos?, tintIndex: Int): Int {
        world?.let {
            val entity = world.getBlockEntity(pos)
            if(entity is NodeBlockEntity){
                entity.node?.let {
                    return if(tintIndex==0 || entity.node?.code(tintIndex-1)==true){
                         ColorTools.int(it.color)
                    }
                    else{
                        val color=it.color.copy()
                        val direction=if((color.x+color.y+color.z)>0.3f) -0.2f else 0.2f
                        color.add(direction,direction,direction)
                        color.clamp(0f,1f)
                        ColorTools.int(color)
                    }
                }
            }
        }
        return DyeColor.GRAY.fireworkColor
    }
}