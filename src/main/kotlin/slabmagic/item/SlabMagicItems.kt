package slabmagic.item

import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import slabmagic.SlabMagicMod
import slabmagic.block.SlabMagicBlocks

object SlabMagicItems {

    fun <T: Item> register(id: String, item: T): T{
        Registry.register(Registries.ITEM, SlabMagicMod.id(id), item)
        return item
    }

    val ACTIVATOR_WAND = register("activator_wand", WandItem(WandItem::castAndConsume, Item.Settings().maxCount(1)))
    val ULTRA_WAND = register("ultra_wand", WandItem(WandItem::cast, Item.Settings().maxCount(1)))
    val COST_WAND = register("cost_wand", WandItem(WandItem::cost, Item.Settings().maxCount(1)))
    val DESC_WAND = register("desc_wand", WandItem(WandItem::resume, Item.Settings().maxCount(1)))

    val ACTIVATOR_CONCENTRATOR = register("activator_concentrator", BlockItem(SlabMagicBlocks.ACTIVATOR_CONCENTRATOR, Item.Settings()))
    val UPGRADED_ACTIVATOR_CONCENTRATOR = register("upgraded_activator_concentrator", BlockItem(SlabMagicBlocks.UPGRADED_ACTIVATOR_CONCENTRATOR, Item.Settings()))
    val COPPER_ROBOT = register("copper_robot", BlockItem(SlabMagicBlocks.COPPER_ROBOT, Item.Settings()))
    val GOLD_ROBOT = register("gold_robot", BlockItem(SlabMagicBlocks.GOLD_ROBOT, Item.Settings()))
    val ANCIENT_ROBOT = register("ancient_robot", BlockItem(SlabMagicBlocks.ANCIENT_ROBOT, Item.Settings()))

    val REDSTONE_HEART = register("redstone_heart", BlockItem(SlabMagicBlocks.REDSTONE_HEART, Item.Settings()))
    val CONTAMINATED_REDSTONE_HEART = register("contaminated_redstone_heart", BlockItem(SlabMagicBlocks.CONTAMINATED_REDSTONE_HEART, Item.Settings()))

    val REDSTONE_EYE = register("redstone_eye", BlockItem(SlabMagicBlocks.REDSTONE_EYE, Item.Settings()))
    val CONTAMINATED_REDSTONE_EYE = register("contaminated_redstone_eye", BlockItem(SlabMagicBlocks.CONTAMINATED_REDSTONE_EYE, Item.Settings()))

    val CONDUCTOR = register("conductor", BlockItem(SlabMagicBlocks.CONDUCTOR, Item.Settings()))

    val COPPER_BATTERY = register("copper_battery", BlockItem(SlabMagicBlocks.COPPER_BATTERY, Item.Settings()))
    val GOLD_BATTERY = register("gold_battery", BlockItem(SlabMagicBlocks.GOLD_BATTERY, Item.Settings()))
    val IRON_BATTERY = register("iron_battery", BlockItem(SlabMagicBlocks.IRON_BATTERY, Item.Settings()))
    val ENERGY_BATTERY = register("energy_battery", BlockItem(SlabMagicBlocks.ENERGY_BATTERY, Item.Settings()))

    data class CopperItems(val unwaxed: Array<BlockItem>, val waxed: Array<BlockItem>): Iterable<BlockItem>{
        override fun iterator() = (unwaxed.asSequence() + waxed.asSequence()).iterator()
    }
    fun createCoppers(id: String, blocks: SlabMagicBlocks.CopperBlocks, settings: Item.Settings.()->Unit={}): CopperItems{
        return CopperItems(
            arrayOf(
                register(id, BlockItem(blocks.unwaxed[0], Item.Settings().apply { settings() })),
                register("exposed_$id", BlockItem(blocks.unwaxed[1], Item.Settings().apply { settings() })),
                register("weathered_$id", BlockItem(blocks.unwaxed[2], Item.Settings().apply { settings() })),
                register("oxidised_$id", BlockItem(blocks.unwaxed[3], Item.Settings().apply { settings() })),
            ),
            arrayOf(
                register("waxed_$id", BlockItem(blocks.waxed[0], Item.Settings().apply { settings() })),
                register("waxed_exposed_$id", BlockItem(blocks.waxed[1], Item.Settings().apply { settings() })),
                register("waxed_weathered_$id", BlockItem(blocks.waxed[2], Item.Settings().apply { settings() })),
                register("waxed_oxidised_$id", BlockItem(blocks.waxed[3], Item.Settings().apply { settings() }))
            )
        )
    }
    val COPPER_GRATE = createCoppers("copper_grate", SlabMagicBlocks.COPPER_GRATE)
    val COPPER_WINDOW = createCoppers("copper_window", SlabMagicBlocks.COPPER_WINDOW)

    val COPPER_BRICKS = createCoppers("copper_bricks", SlabMagicBlocks.COPPER_BRICKS)
    val CHISELED_COPPER = createCoppers("chiseled_copper", SlabMagicBlocks.CHISELED_COPPER)
    val METAL_SANDWICH = createCoppers("metal_sandwich", SlabMagicBlocks.METAL_SANDWICH)

    val SLAB = register("slab", NodeBlockItem(SlabMagicBlocks.SLAB, Item.Settings()))
    val OLD_SLAB = register("old_slab", NodeBlockItem(SlabMagicBlocks.OLD_SLAB, Item.Settings()))
    val ENERGY_SLAB = register("energy_slab", NodeBlockItem(SlabMagicBlocks.ENERGY_SLAB, Item.Settings()))

    val SPELL_ORB = register("spell_orb", SpellCastingItem(Item.Settings().maxDamage(10)))
    val SPELL_WAND = register("spell_wand", SpellCastingItem(Item.Settings().maxDamage(64)))
    val SPELL_DUST = register("spell_dust", SpellCastingItem(Item.Settings()))
    val SPELL_SWORD = register("spell_sword", SpellCastingWeaponItem(Item.Settings()))

    val LENS= register("lens", Item(Item.Settings().maxCount(1)))

}