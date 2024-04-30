package slabmagic.item

import net.minecraft.block.Block
import net.minecraft.client.item.TooltipType
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import slabmagic.item.helper.SpellItemHelpers

class NodeBlockItem(block: Block, settings: Settings) : BlockItem(block, settings) {

    override fun getName(stack: ItemStack) = SpellItemHelpers.partName(stack)

    override fun appendTooltip(stack: ItemStack, context: TooltipContext, tooltip: MutableList<Text>, type: TooltipType) {
        SpellItemHelpers.partTooltip(stack, context, tooltip, type)
        super.appendTooltip(stack, context, tooltip, type)
    }
}