package assets.`slate-magic`.block

import assets.`slate-magic`.block.color.NodeBlockColor
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import slatemagic.block.SlateMagicBlocks

object SlateMagicBlockColors {
    init{
        ColorProviderRegistry.BLOCK.register(NodeBlockColor, SlateMagicBlocks.SLATE_BLOCK)
    }
}