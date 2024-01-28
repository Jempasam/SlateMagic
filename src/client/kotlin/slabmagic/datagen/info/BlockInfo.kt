package slabmagic.datagen.info

import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import slabmagic.datagen.SlabMagicLanguageGenerator
import slabmagic.datagen.tools.SamModelGenerator


typealias BlockInfoSupplier<T> = (T.(Block)->Unit)?
data class BlockInfo(
    val block: Block,
    val translation: BlockInfoSupplier<SlabMagicLanguageGenerator.Builder> = null,
    val model: BlockInfoSupplier<SamModelGenerator> = null,
    val loot: BlockInfoSupplier<FabricBlockLootTableProvider> = null,
    val tags: BlockInfoSupplier<FabricTagProvider<Block>> = null
)