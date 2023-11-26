package slatemagic.item.color

import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.item.ItemStack
import slatemagic.item.SpellItem

object SpellItemColor: ItemColorProvider {
    override fun getColor(stack: ItemStack, tintIndex: Int): Int {
        val item=stack.item
        return if(item is SpellItem && tintIndex==0) item.getColor(stack) else 0xFFFFFF
    }
}