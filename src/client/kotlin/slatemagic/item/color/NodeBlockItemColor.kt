package assets.`slate-magic`.block.color

import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.item.ItemStack
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import slatemagic.helper.ColorTools
import slatemagic.registry.SlateMagicRegistry

object NodeBlockItemColor: ItemColorProvider {
    override fun getColor(stack: ItemStack, tintIndex: Int): Int {
        return stack.nbt
            ?.getCompound("BlockEntityTag")
            ?.getString("node")
            ?.let { Identifier.tryParse(it) }
            ?.let { SlateMagicRegistry.NODES.get(it) }
            ?.let { ColorTools.int(it.color) }
            ?: DyeColor.GRAY.fireworkColor
    }
}