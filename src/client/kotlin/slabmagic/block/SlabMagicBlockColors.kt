package assets.`slab-magic`.block

import slabmagic.block.color.NodeBlockColor
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import slabmagic.block.SlabMagicBlocks

object SlabMagicBlockColors {
    init{
        ColorProviderRegistry.BLOCK.register(NodeBlockColor, SlabMagicBlocks.SLAB)
        ColorProviderRegistry.BLOCK.register(NodeBlockColor, SlabMagicBlocks.OLD_SLAB)
        ColorProviderRegistry.BLOCK.register(NodeBlockColor, SlabMagicBlocks.ENERGY_SLAB)
    }
}