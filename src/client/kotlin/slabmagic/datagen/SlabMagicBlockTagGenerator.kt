package slabmagic.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.tag.BlockTags
import net.minecraft.util.registry.Registry
import slabmagic.block.SlabMagicBlocks

fun <T> FabricTagProvider<*>.FabricTagBuilder<T>.addAll(iterable: Iterable<T>): FabricTagProvider<*>.FabricTagBuilder<T>{
    iterable.forEach { add(it) }
    return this
}

class SlabMagicBlockTagGenerator(dataGenerator: FabricDataGenerator) : FabricTagProvider<Block>(dataGenerator, Registry.BLOCK) {
    override fun generateTags() {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)

            .add(SlabMagicBlocks.REDSTONE_HEART)
            .add(SlabMagicBlocks.REDSTONE_EYE)
            .add(SlabMagicBlocks.CONTAMINATED_REDSTONE_HEART)
            .add(SlabMagicBlocks.CONTAMINATED_REDSTONE_EYE)
            .add(SlabMagicBlocks.COPPER_BATTERY)
            .add(SlabMagicBlocks.GOLD_BATTERY)
            .add(SlabMagicBlocks.IRON_BATTERY)
            .add(SlabMagicBlocks.CONDUCTOR)

            .add(SlabMagicBlocks.SLAB)

            .addAll(SlabMagicBlocks.COPPER_BRICKS)
            .addAll(SlabMagicBlocks.CHISELED_COPPER)
            .addAll(SlabMagicBlocks.METAL_SANDWICH)

            .addAll(SlabMagicBlocks.COPPER_GRATE)
            .addAll(SlabMagicBlocks.COPPER_WINDOW)

    }
}