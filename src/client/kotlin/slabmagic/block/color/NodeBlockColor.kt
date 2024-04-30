package slabmagic.block.color

import net.minecraft.block.BlockState
import net.minecraft.client.color.block.BlockColorProvider
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView
import org.joml.Vector3f
import slabmagic.components.SlabMagicComponents
import slabmagic.helper.ColorTools
import slabmagic.spell.build.parts.code
import slabmagic.utils.coerceIn

object NodeBlockColor: BlockColorProvider {
    override fun getColor(state: BlockState, world: BlockRenderView?, pos: BlockPos?, tintIndex: Int): Int {
        world?.let {
            val entity = world.getBlockEntity(pos)
            val part = entity?.components?.get(SlabMagicComponents.PART)?.value()
            if(part!=null){
                return if(tintIndex==0 || part.code(tintIndex-1)){
                     ColorTools.int(part.color)
                }
                else{
                    val color=Vector3f(part.color)
                    val direction=if((color.x+color.y+color.z)>0.3f) -0.2f else 0.2f
                    color.add(direction,direction,direction)
                    color.coerceIn(0f,1f)
                    ColorTools.int(color)
                }
            }
        }
        return DyeColor.GRAY.fireworkColor
    }
}