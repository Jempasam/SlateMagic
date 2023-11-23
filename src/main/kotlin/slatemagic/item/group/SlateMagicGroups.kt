package slatemagic.item.group

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemStack
import slatemagic.SlateMagicMod
import slatemagic.item.SlateMagicItems
import slatemagic.registry.SlateMagicRegistry

object SlateMagicGroups {
    fun getSlateIcon(): ItemStack = ItemStack(SlateMagicItems.SLATE).also { stack ->
        SlateMagicRegistry.NODES.first().let { node -> SlateMagicItems.SLATE.setNode(stack,node) }
    }
    val SLATES=FabricItemGroupBuilder.create(SlateMagicMod.id("slates")).icon(::getSlateIcon).build()
}