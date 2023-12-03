package assets.`slab-magic`.block

import assets.`slab-magic`.block.color.NodeBlockItemColor
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import slabmagic.item.SlabMagicItems
import slabmagic.item.color.SpellItemColor

object SlabMagicItemColors {
    init{
        ColorProviderRegistry.ITEM.register(NodeBlockItemColor, SlabMagicItems.SLAB)
        ColorProviderRegistry.ITEM.register(SpellItemColor, SlabMagicItems.SPELL_ORB)
        ColorProviderRegistry.ITEM.register(SpellItemColor, SlabMagicItems.SPELL_DUST)
        ColorProviderRegistry.ITEM.register(SpellItemColor, SlabMagicItems.SPELL_SWORD)
    }
}