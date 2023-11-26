package slatemagic.item

import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry
import slatemagic.SlateMagicMod
import slatemagic.block.SlateMagicBlocks
import slatemagic.item.group.SlateMagicGroups

object SlateMagicItems {

    fun <T: Item> register(id: String, item: T): T{
        Registry.register(Registry.ITEM, SlateMagicMod.id(id), item)
        return item
    }
    val ACTIVATOR = register("activator", ActivatorItem(Item.Settings().group(SlateMagicGroups.TOOLS)))

    val ACTIVATOR_BLOCK = register("activator_block", BlockItem(SlateMagicBlocks.ACTIVATOR_BLOCK, Item.Settings().group(SlateMagicGroups.TOOLS)))
    val SMART_ACTIVATOR_BLOCK = register("smart_activator_block", BlockItem(SlateMagicBlocks.SMART_ACTIVATOR_BLOCK, Item.Settings().group(SlateMagicGroups.TOOLS)))

    val SLATE = register("slate_block", NodeBlockItem(SlateMagicBlocks.SLATE_BLOCK, Item.Settings().group(SlateMagicGroups.SLATES)))

    val SPELL_ORB = register("spell_orb", SpellCastingItem(Item.Settings().group(SlateMagicGroups.SPELLS).maxDamage(10)))
    val SPELL_DUST = register("spell_dust", SpellCastingItem(Item.Settings().group(SlateMagicGroups.SPELLS)))
}