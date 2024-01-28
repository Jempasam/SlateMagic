package slabmagic.ui

import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import slabmagic.block.entity.PartBlockEntity
import slabmagic.helper.ColorTools
import slabmagic.item.SlabMagicItems

object SlabHudProvider: GlassesHudRenderCallback.Provider {
    override fun get(state: BlockState, pos: BlockPos): List<GlassesHudRenderCallback.Part> {
        val block= MinecraftClient.getInstance().world?.getBlockEntity(pos)
        return if(block is PartBlockEntity) {
            val (stack, text) = block.part?.let {
                val color = Style.EMPTY.withColor(ColorTools.int(it.color))
                val text = it.name.copy().setStyle(color).apply {
                    if(it.parameters.isNotEmpty()) append(" -> ")
                    for (p in it.parameters) append(p.name).append(", ")
                    if (it.parameters.isNotEmpty()) siblings.removeLast()
                }
                val stack = ItemStack(SlabMagicItems.SLAB)
                SlabMagicItems.SLAB.setNode(stack, it)
                stack to text
            } ?: (ItemStack(Items.GUNPOWDER) to Text.of("No Node"))

            listOf(GlassesHudRenderCallback.Part(stack, text))
        }
        else{
            listOf()
        }
    }
}