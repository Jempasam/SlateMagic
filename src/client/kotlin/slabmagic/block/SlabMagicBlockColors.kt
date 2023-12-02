package assets.`slab-magic`.block

import assets.`slab-magic`.block.color.NodeBlockColor
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import slabmagic.block.SlabMagicBlocks

object SlabMagicBlockColors {
    init{
        ColorProviderRegistry.BLOCK.register(NodeBlockColor, SlabMagicBlocks.SLAB)
    }
}