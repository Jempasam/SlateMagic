package assets.`slate-magic`.block

import assets.`slate-magic`.block.color.NodeBlockItemColor
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import slatemagic.item.SlateMagicItems
import slatemagic.item.color.SpellItemColor

object SlateMagicItemColors {
    init{
        ColorProviderRegistry.ITEM.register(NodeBlockItemColor, SlateMagicItems.SLATE)
        ColorProviderRegistry.ITEM.register(SpellItemColor, SlateMagicItems.SPELL_ORB)
        ColorProviderRegistry.ITEM.register(SpellItemColor, SlateMagicItems.SPELL_DUST)
    }
}