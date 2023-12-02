package slabmagic.item

import net.minecraft.block.Block
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.World
import slabmagic.SlabMagicMod
import slabmagic.helper.ColorTools
import slabmagic.registry.SlabMagicRegistry
import slabmagic.spell.build.parts.SpellPart

class NodeBlockItem(block: Block, settings: Settings) : BlockItem(block, settings) {

    fun getNode(stack: ItemStack) = stack.nbt
        ?.getCompound("BlockEntityTag")
        ?.getString("node")
        ?.let { Identifier.tryParse(it) }
        ?.let { SlabMagicRegistry.PARTS.get(it) }

    fun setNode(stack: ItemStack, node: SpellPart<*>) {
        SlabMagicRegistry.PARTS.getId(node)?.let {
            stack.orCreateNbt.put("BlockEntityTag", NbtCompound().apply{ putString("node", it.toString()) })
        }
    }

    override fun appendStacks(group: ItemGroup, stacks: DefaultedList<ItemStack>) {
        if(isIn(group)){
            stacks.add(ItemStack(this))
            SlabMagicRegistry.PARTS.forEach { node ->
                stacks.add(ItemStack(this).apply{ setNode(this, node) })
            }
        }
    }

    override fun getName(stack: ItemStack): Text {
        return getNode(stack)
            ?.let { Text.translatable(translationKey,it.name) .setStyle(Style.EMPTY.withColor(ColorTools.int(it.color))) }
            ?: Text.translatable("$translationKey.empty")
    }

    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        val node=getNode(stack)
        if(node==null){
            tooltip.add(SlabMagicMod.translatable("tooltip","empty"))
        }
        else{
            val style=Style.EMPTY.withColor(ColorTools.int(node.color))
            tooltip.add(node.name.copy().setStyle(style))
            tooltip.add(node.desc.copy().setStyle(style))

            val text=SlabMagicMod.translatable("tooltip","arguments")
            for(param in node.parameters)text.append("(").append(param.name).append(")")
            tooltip.add(text)
        }
        super.appendTooltip(stack, world, tooltip, context)
    }
}