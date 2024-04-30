package assets.`slab-magic`.block.color

import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.item.ItemStack
import net.minecraft.util.DyeColor
import slabmagic.components.SlabMagicComponents
import slabmagic.helper.ColorTools

object NodeBlockItemColor: ItemColorProvider {
    override fun getColor(stack: ItemStack, tintIndex: Int): Int {
        return stack.get(SlabMagicComponents.PART)
            ?.value() ?.color ?.let{ColorTools.int(it)}
            ?: DyeColor.GRAY.fireworkColor
    }
}