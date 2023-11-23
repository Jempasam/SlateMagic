package slatemagic.item

import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import slatemagic.helper.ColorTools
import slatemagic.registry.SlateMagicRegistry
import slatemagic.spell.build.SpellNode

class NodeBlockItem(block: Block, settings: Settings) : BlockItem(block, settings) {

    fun getNode(stack: ItemStack) = stack.nbt
        ?.getCompound("BlockEntityTag")
        ?.getString("node")
        ?.let { Identifier.tryParse(it) }
        ?.let { SlateMagicRegistry.NODES.get(it) }

    fun setNode(stack: ItemStack, node: SpellNode<*>) {
        SlateMagicRegistry.NODES.getId(node)?.let {
            stack.orCreateNbt.put("BlockEntityTag", NbtCompound().apply{ putString("node", it.toString()) })
        }
    }

    override fun appendStacks(group: ItemGroup, stacks: DefaultedList<ItemStack>) {
        if(isIn(group)){
            stacks.add(ItemStack(this))
            SlateMagicRegistry.NODES.forEach { node ->
                stacks.add(ItemStack(this).apply{ setNode(this, node) })
            }
        }
    }

    override fun getName(stack: ItemStack): Text {
        return getNode(stack)
            ?.let { Text.translatable(translationKey,it.name) .setStyle(Style.EMPTY.withColor(ColorTools.int(it.color))) }
            ?: Text.translatable("$translationKey.empty")
    }
}