package slabmagic.datagen.info

import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.item.Item
import slabmagic.datagen.SlabMagicLanguageGenerator


typealias ItemInfoSupplier<T> = (T.(Item)->Unit)?
data class ItemInfo(
    val item: Item,
    val translation: BlockInfoSupplier<SlabMagicLanguageGenerator.Builder> = null,
    val model: BlockInfoSupplier<ItemModelGenerator> = null,
    val tags: BlockInfoSupplier<FabricTagProvider<Item>> = null,
)