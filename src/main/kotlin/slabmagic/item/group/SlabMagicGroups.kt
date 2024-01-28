package slabmagic.item.group

import com.unascribed.lib39.fractal.api.ItemSubGroup
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemStack
import slabmagic.SlabMagicMod
import slabmagic.item.SlabMagicItems
import slabmagic.registry.SlabMagicRegistry

object SlabMagicGroups {
    fun getSlabIcon(): ItemStack = ItemStack(SlabMagicItems.SLAB).also { stack ->
        SlabMagicRegistry.PARTS.first().let { node -> SlabMagicItems.SLAB.setNode(stack,node) }
    }

    val MAIN=FabricItemGroupBuilder.create(SlabMagicMod.id("main")).icon(::getSlabIcon).build()
    val TOOLS=ItemSubGroup.create(MAIN, SlabMagicMod.id("tools"))
    val SLABS=ItemSubGroup.create(MAIN, SlabMagicMod.id("slabs"))
    val OLD_SLABS=ItemSubGroup.create(MAIN, SlabMagicMod.id("old_slabs"))
    val ENERGY_SLABS=ItemSubGroup.create(MAIN, SlabMagicMod.id("energy_slabs"))
    val SPELLS=ItemSubGroup.create(MAIN, SlabMagicMod.id("spells"))
    val BUILDINGS=ItemSubGroup.create(MAIN, SlabMagicMod.id("buildings"))

}