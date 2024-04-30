package slabmagic.item.helper

import net.minecraft.client.item.TooltipType
import net.minecraft.item.Item
import net.minecraft.item.Item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import slabmagic.SlabMagicMod
import slabmagic.helper.ColorTools
import slabmagic.components.SlabMagicComponents as Component

object SpellItemHelpers {

    fun spellName(stack: ItemStack): MutableText{
        return Text.translatable(
            stack.translationKey,
            stack.get(Component.SPELL) ?.effect ?.name ?: Text.empty(),
            stack.get(Component.STORED_CONTEXT) ?.power ?: 1
        )
    }

    fun spellTooltip(stack: ItemStack, context: Item.TooltipContext, tooltip: MutableList<Text>, type: TooltipType) {
        val (name,color)= stack.get(Component.SPELL)
            ?.let{ it.effect.name to ColorTools.int(it.effect.color) }
            ?: (Text.empty() to DyeColor.WHITE.fireworkColor)

        val power= stack.get(Component.STORED_CONTEXT)
            ?.power
            ?: 1
        tooltip.add(Text.empty() .append(name) .append(" ") .append(power.toString()) .withColor(color))
    }

    fun partName(stack: ItemStack): Text {
        return stack.get(Component.PART)?.value()
            ?.let { Text.translatable(stack.translationKey,it.name).withColor(ColorTools.int(it.color)) }
            ?: Text.translatable("${stack.translationKey}.empty")
    }

    fun partTooltip(stack: ItemStack, context: TooltipContext, tooltip: MutableList<Text>, type: TooltipType) {
        val node=stack.get(Component.PART)?.value()
        if(node==null){
            tooltip.add(SlabMagicMod.translatable("tooltip","empty"))
        }
        else{
            val style= Style.EMPTY.withColor(ColorTools.int(node.color))
            tooltip.add(node.name.copy().setStyle(style))
            tooltip.add(node.desc.copy().setStyle(style))

            val text= SlabMagicMod.translatable("tooltip","arguments")
            for(param in node.parameters)text.append("(").append(param.name).append(")")
            tooltip.add(text)
        }
    }
}