package slatemagic.item.group

import com.unascribed.lib39.fractal.api.ItemSubGroup
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemStack
import slatemagic.SlateMagicMod
import slatemagic.item.SlateMagicItems
import slatemagic.registry.SlateMagicRegistry

object SlateMagicGroups {
    fun getSlateIcon(): ItemStack = ItemStack(SlateMagicItems.SLATE).also { stack ->
        SlateMagicRegistry.NODES.first().let { node -> SlateMagicItems.SLATE.setNode(stack,node) }
    }

    val SLATE_MAGIC=FabricItemGroupBuilder.create(SlateMagicMod.id("main")).icon(::getSlateIcon).build()
    val TOOLS=ItemSubGroup.create(SLATE_MAGIC, SlateMagicMod.id("tools"))
    val SLATES=ItemSubGroup.create(SLATE_MAGIC, SlateMagicMod.id("slates"))
    val SPELLS=ItemSubGroup.create(SLATE_MAGIC, SlateMagicMod.id("spells"))

}