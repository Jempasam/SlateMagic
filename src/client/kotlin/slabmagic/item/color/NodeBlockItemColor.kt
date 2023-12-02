package assets.`slab-magic`.block.color

import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.item.ItemStack
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import slabmagic.helper.ColorTools
import slabmagic.registry.SlabMagicRegistry
import slabmagic.spell.build.parts.code

object NodeBlockItemColor: ItemColorProvider {
    override fun getColor(stack: ItemStack, tintIndex: Int): Int {
        return stack.nbt
            ?.getCompound("BlockEntityTag")
            ?.getString("node")
            ?.let { Identifier.tryParse(it) }
            ?.let { SlabMagicRegistry.PARTS.get(it) }
            ?.let {
                if(tintIndex==0 || it.code(tintIndex-1)){
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
            ?: DyeColor.GRAY.fireworkColor
    }
}