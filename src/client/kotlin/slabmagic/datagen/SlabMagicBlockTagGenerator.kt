package slabmagic.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import slabmagic.block.SlabMagicBlocks
import java.util.concurrent.CompletableFuture

fun <T> FabricTagProvider<T>.FabricTagBuilder.addAll(iterable: Iterable<T>): FabricTagProvider<T>.FabricTagBuilder{
    iterable.forEach { add(it) }
    return this
}

class SlabMagicBlockTagGenerator(dataGenerator: FabricDataOutput, reg: CompletableFuture<RegistryWrapper.WrapperLookup>) : FabricTagProvider<Block>(dataGenerator, RegistryKeys.BLOCK, reg) {
    override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup){
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