package slabmagic.item.group

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import samoussalib.builder.stack
import slabmagic.SlabMagicMod
import slabmagic.item.SlabMagicItems
import slabmagic.registry.SlabMagicRegistry
import slabmagic.components.SlabMagicComponents as Component

object SlabMagicGroups {

    fun getIcon(item: Item) = stack(item){
            SlabMagicRegistry.PARTS.streamEntries().findFirst().ifPresent { add(Component.PART, it) }
            SlabMagicRegistry.EFFECTS.streamEntries().findFirst().ifPresent { add(Component.SPELL, it.value()) }
        }

    fun register(id: String, builder: ItemGroup.Builder.()->Unit): ItemGroup {
        val builder = FabricItemGroup.builder()
        builder.builder()
        builder.displayName(SlabMagicMod.translatable("itemGroup",id))
        val group=builder.build()
        Registry.register(Registries.ITEM_GROUP, SlabMagicMod.id(id), group)
        return group
    }

    val TOOLS= register("tools"){
        icon{SlabMagicItems.LENS.defaultStack}
        entries { context, entries ->
            entries.add(SlabMagicItems.ACTIVATOR_WAND)
            entries.add(SlabMagicItems.ULTRA_WAND)
            entries.add(SlabMagicItems.COST_WAND)
            entries.add(SlabMagicItems.DESC_WAND)

            entries.add(SlabMagicItems.LENS)

            entries.add(SlabMagicItems.ACTIVATOR_CONCENTRATOR)
            entries.add(SlabMagicItems.UPGRADED_ACTIVATOR_CONCENTRATOR)
            entries.add(SlabMagicItems.COPPER_ROBOT)
            entries.add(SlabMagicItems.GOLD_ROBOT)
            entries.add(SlabMagicItems.ANCIENT_ROBOT)

            entries.add(SlabMagicItems.REDSTONE_HEART)
            entries.add(SlabMagicItems.CONTAMINATED_REDSTONE_HEART)

            entries.add(SlabMagicItems.REDSTONE_EYE)
            entries.add(SlabMagicItems.CONTAMINATED_REDSTONE_EYE)

            entries.add(SlabMagicItems.CONDUCTOR)

            entries.add(SlabMagicItems.COPPER_BATTERY)
            entries.add(SlabMagicItems.GOLD_BATTERY)
            entries.add(SlabMagicItems.IRON_BATTERY)
            entries.add(SlabMagicItems.ENERGY_BATTERY)
        }
    }

    val SLABS= register("slabs"){
        icon{getIcon(SlabMagicItems.SLAB)}
        entries { context, entries ->
            for(part in SlabMagicRegistry.PARTS.streamEntries()) entries.add(stack(SlabMagicItems.SLAB){add(Component.PART,part)})
        }
    }

    val OLD_SLABS= register("old_slabs"){
        icon{getIcon(SlabMagicItems.OLD_SLAB)}
        entries { context, entries ->
            for(part in SlabMagicRegistry.PARTS.streamEntries()) entries.add(stack(SlabMagicItems.OLD_SLAB){add(Component.PART,part)})
        }
    }

    val ENERGY_SLABS= register("energy_slabs"){
        icon{getIcon(SlabMagicItems.ENERGY_SLAB)}
        entries { context, entries ->
            for(part in SlabMagicRegistry.PARTS.streamEntries()) entries.add(stack(SlabMagicItems.ENERGY_SLAB){add(Component.PART,part)})
        }
    }

    val SPELLS= register("spells"){
        icon{getIcon(SlabMagicItems.SPELL_WAND)}
        entries { context, entries ->
            for(spell in SlabMagicRegistry.EFFECTS){
                entries.add(stack(SlabMagicItems.SPELL_ORB){add(Component.SPELL,spell)})
                entries.add(stack(SlabMagicItems.SPELL_WAND){add(Component.SPELL,spell)})
                entries.add(stack(SlabMagicItems.SPELL_DUST){add(Component.SPELL,spell)})
                entries.add(stack(SlabMagicItems.SPELL_SWORD){add(Component.SPELL,spell)})
            }
        }
    }

    val BUILDINGS= register("buildings"){
        icon{getIcon(SlabMagicItems.COPPER_GRATE.unwaxed[0])}
        entries { context, entries ->
            SlabMagicItems.COPPER_GRATE.forEach{entries.add(it)}
            SlabMagicItems.COPPER_WINDOW.forEach{entries.add(it)}

            SlabMagicItems.COPPER_BRICKS.forEach{entries.add(it)}
            SlabMagicItems.CHISELED_COPPER.forEach{entries.add(it)}
            SlabMagicItems.METAL_SANDWICH.forEach{entries.add(it)}
        }
    }

}