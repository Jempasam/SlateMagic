package slabmagic.item

import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry
import slabmagic.SlabMagicMod
import slabmagic.block.SlabMagicBlocks
import slabmagic.item.group.SlabMagicGroups

object SlabMagicItems {

    fun <T: Item> register(id: String, item: T): T{
        Registry.register(Registry.ITEM, SlabMagicMod.id(id), item)
        return item
    }

    val ACTIVATOR = register("activator", ActivatorItem(Item.Settings().group(SlabMagicGroups.TOOLS).maxCount(1)))

    val ACTIVATOR_WAND = register("activator_wand", WandItem(WandItem::cast, Item.Settings().group(SlabMagicGroups.TOOLS).maxCount(1)))
    val COST_WAND = register("cost_wand", WandItem(WandItem::cost, Item.Settings().group(SlabMagicGroups.TOOLS).maxCount(1)))
    val DESC_WAND = register("desc_wand", WandItem(WandItem::resume, Item.Settings().group(SlabMagicGroups.TOOLS).maxCount(1)))

    val ACTIVATOR_BLOCK = register("activator_block", BlockItem(SlabMagicBlocks.ACTIVATOR_BLOCK, Item.Settings().group(SlabMagicGroups.TOOLS)))
    val SMART_ACTIVATOR_BLOCK = register("smart_activator_block", BlockItem(SlabMagicBlocks.SMART_ACTIVATOR_BLOCK, Item.Settings().group(SlabMagicGroups.TOOLS)))

    val ACTIVATOR_CONCENTRATOR = register("activator_concentrator", BlockItem(SlabMagicBlocks.ACTIVATOR_CONCENTRATOR, Item.Settings().group(SlabMagicGroups.TOOLS)))
    val UPGRADED_ACTIVATOR_CONCENTRATOR = register("upgraded_activator_concentrator", BlockItem(SlabMagicBlocks.UPGRADED_ACTIVATOR_CONCENTRATOR, Item.Settings().group(SlabMagicGroups.TOOLS)))

    val REDSTONE_HEART = register("redstone_heart", BlockItem(SlabMagicBlocks.REDSTONE_HEART, Item.Settings().group(SlabMagicGroups.TOOLS)))
    val CONTAMINATED_REDSTONE_HEART = register("contaminated_redstone_heart", BlockItem(SlabMagicBlocks.CONTAMINATED_REDSTONE_HEART, Item.Settings().group(SlabMagicGroups.TOOLS)))

    val SLAB = register("slab", NodeBlockItem(SlabMagicBlocks.SLAB, Item.Settings().group(SlabMagicGroups.SLABS)))

    val SPELL_ORB = register("spell_orb", SpellCastingItem(Item.Settings().group(SlabMagicGroups.SPELLS).maxDamage(10)))
    val SPELL_DUST = register("spell_dust", SpellCastingItem(Item.Settings().group(SlabMagicGroups.SPELLS)))
    val SPELL_SWORD = register("spell_sword", SpellCastingWeaponItem(Item.Settings().group(SlabMagicGroups.SPELLS)))

    val LENS= register("lens", Item(Item.Settings().group(SlabMagicGroups.TOOLS).maxCount(1)))
}