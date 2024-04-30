package slabmagic.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.block.Block
import net.minecraft.client.recipebook.RecipeBookGroup
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory.BUILDING_BLOCKS
import net.minecraft.registry.RegistryWrapper
import slabmagic.block.SlabMagicBlocks
import slabmagic.item.SlabMagicItems
import java.util.concurrent.CompletableFuture

class SlabMagicRecipeGenerator(dataGenerator: FabricDataOutput, reg: CompletableFuture<RegistryWrapper.WrapperLookup>) : FabricRecipeProvider(dataGenerator,reg) {
    override fun generate(exporter: RecipeExporter) {
        val coppers= arrayOf(
            Items.COPPER_BLOCK, Items.EXPOSED_COPPER, Items.WEATHERED_COPPER, Items.OXIDIZED_COPPER,
            Items.WAXED_COPPER_BLOCK, Items.WAXED_EXPOSED_COPPER, Items.WAXED_WEATHERED_COPPER, Items.WAXED_OXIDIZED_COPPER
            )

        val cut_coppers= arrayOf(
            Items.CUT_COPPER, Items.EXPOSED_CUT_COPPER, Items.WEATHERED_CUT_COPPER, Items.OXIDIZED_CUT_COPPER,
            Items.WAXED_CUT_COPPER, Items.WAXED_EXPOSED_CUT_COPPER, Items.WAXED_WEATHERED_CUT_COPPER, Items.WAXED_OXIDIZED_CUT_COPPER
        )

        fun cuttingRecipe(from: Item, to: Item) {
            println("cutting ${from.name.string} to ${to.name.string}")
            ShapedRecipeJsonBuilder.create(BUILDING_BLOCKS,to,4)
                .input('a', from)
                .pattern("aa")
                .pattern("aa")
                .criterion("has_ingredient", conditionsFromItem(from))
                .group(RecipeBookGroup.CRAFTING_BUILDING_BLOCKS.name)
                .offerTo(exporter)
        }

        fun grid(from: Item, to: Block, count: Int = 1) {
            ShapedRecipeJsonBuilder.create(BUILDING_BLOCKS,to,count)
                .input('a', from)
                .pattern("a a")
                .pattern(" a ")
                .pattern("a a")
                .criterion("has_ingredient", conditionsFromItem(from))
                .group(RecipeBookGroup.CRAFTING_BUILDING_BLOCKS.name)
                .offerTo(exporter)
        }

    /* COPPER BLOCKS */
        /* CUT */
        cut_coppers.zip(SlabMagicItems.COPPER_BRICKS) .forEach { (a, b) -> cuttingRecipe(a,b) }
        SlabMagicItems.COPPER_BRICKS.zip(SlabMagicItems.CHISELED_COPPER) .forEach { (a, b) -> cuttingRecipe(a,b) }

        /* GRID */
        grid(Items.COPPER_INGOT, SlabMagicBlocks.COPPER_GRATE.unwaxed[0])
        coppers.zip(SlabMagicBlocks.COPPER_WINDOW).forEach { (a, b) -> grid(a,b) }

        /* METAL SANDWICH */
        ShapedRecipeJsonBuilder.create(BUILDING_BLOCKS,SlabMagicItems.METAL_SANDWICH.unwaxed[0])
            .input('i',Items.IRON_INGOT).input('c',Items.COPPER_INGOT)
            .pattern("ccc").pattern("iii").pattern("ccc")
            .criterion("has_ingredient", conditionsFromItem(Items.COPPER_INGOT))
            .offerTo(exporter)

        /* WAXING */
        arrayOf(
            SlabMagicItems.CHISELED_COPPER, SlabMagicItems.COPPER_BRICKS,
            SlabMagicItems.COPPER_WINDOW, SlabMagicItems.COPPER_GRATE,
            SlabMagicItems.METAL_SANDWICH
        ).forEach {blocks ->
            for(i in 0..<4){
                ShapelessRecipeJsonBuilder.create(BUILDING_BLOCKS,blocks.waxed[i])
                    .input(blocks.unwaxed[i]).input(Items.HONEYCOMB)
                    .criterion("has_ingredient", conditionsFromItem(blocks.unwaxed[i]))
                    .offerTo(exporter)
            }
        }
    /* */

    }
}