package slabmagic.item.color

import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.item.ItemStack
import slabmagic.components.SlabMagicComponents
import slabmagic.helper.ColorTools

object SpellItemColor: ItemColorProvider {
    override fun getColor(stack: ItemStack, tintIndex: Int): Int {
        return stack.get(SlabMagicComponents.SPELL)
            ?.effect ?.color ?.let{ColorTools.int(it)}
            ?: 0xFFFFFF
    }
}