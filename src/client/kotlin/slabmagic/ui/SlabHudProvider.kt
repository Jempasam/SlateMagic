package slabmagic.ui

import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import slabmagic.components.SlabMagicComponents
import slabmagic.helper.ColorTools
import slabmagic.item.SlabMagicItems
import slabmagic.registry.SlabMagicRegistry
import slabmagic.components.SlabMagicComponents as Component

object SlabHudProvider: GlassesHudRenderCallback.Provider {
    override fun get(state: BlockState, pos: BlockPos): List<GlassesHudRenderCallback.Part> {
        val block= MinecraftClient.getInstance().world?.getBlockEntity(pos)
        val part= block ?.components ?.get(SlabMagicComponents.PART) ?.value()
        return if(part!=null) {
            val (stack, text) = run {
                val color = Style.EMPTY.withColor(ColorTools.int(part.color))
                val text = part.name.copy().setStyle(color).apply {
                    if(part.parameters.isNotEmpty()) append(" -> ")
                    for (p in part.parameters) append(p.name).append(", ")
                    if (part.parameters.isNotEmpty()) siblings.removeLast()
                }
                val stack = ItemStack(SlabMagicItems.SLAB)
                stack.set(Component.PART,SlabMagicRegistry.PARTS.getEntry(part))
                stack to text
            } ?: (ItemStack(Items.GUNPOWDER) to Text.of("No Node"))
            listOf(GlassesHudRenderCallback.Part(stack, text))
        }
        else{
            listOf()
        }
    }
}