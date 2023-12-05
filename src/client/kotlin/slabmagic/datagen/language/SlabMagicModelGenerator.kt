package slabmagic.datagen.language

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.data.client.*
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry
import slabmagic.SlabMagicMod
import slabmagic.block.SlabMagicBlocks
import slabmagic.block.TRIGGERED
import slabmagic.item.SlabMagicItems
import java.util.*

class SlabMagicModelGenerator(dataGenerator: FabricDataGenerator) : FabricModelProvider(dataGenerator){


    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        val samge=SamModelGenerator(blockStateModelGenerator)
        samge.apply{
            blockStateModelGenerator.apply {

                // Contaminable
                val CONTAMINATED_ID=SlabMagicMod.id("block/contaminated")
                val CONTAMINATED_CUBE_ALL=modelChildOf(CONTAMINATED_ID, TextureKey.ALL)

                fun contaminable(block: Block, contaminated: Block){
                    registerOnBool(block, TRIGGERED, TexturedModel.CUBE_ALL, TexturedModel.CUBE_ALL)
                    registerOnBool(contaminated, TRIGGERED,
                        textureAll(CONTAMINATED_CUBE_ALL, textureId(block,"_triggered")),
                        textureAll(CONTAMINATED_CUBE_ALL, textureId(block))
                    )
                }

                contaminable(SlabMagicBlocks.REDSTONE_HEART, SlabMagicBlocks.CONTAMINATED_REDSTONE_HEART)

                // Cube All
                registerSimpleCubeAll(SlabMagicBlocks.ACTIVATOR_BLOCK)
                registerSimpleCubeAll(SlabMagicBlocks.SMART_ACTIVATOR_BLOCK)

                registerSimpleCubeAll(SlabMagicBlocks.ACTIVATOR_CONCENTRATOR)
                registerSimpleCubeAll(SlabMagicBlocks.UPGRADED_ACTIVATOR_CONCENTRATOR)
            }
        }
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
        itemModelGenerator.apply {
            fun child(item: Item, parent: String) = register(item,modelChild(item,parent))

            // Generated
            fun generated(item: Item) = register(item,Models.GENERATED)
            generated(SlabMagicItems.LENS)
            generated(SlabMagicItems.SPELL_DUST)
            generated(SlabMagicItems.SPELL_ORB)

            // Handheld
            fun handheld(item: Item) = register(item,Models.HANDHELD)
            handheld(SlabMagicItems.ACTIVATOR)
            handheld(SlabMagicItems.ACTIVATOR_WAND)
            handheld(SlabMagicItems.COST_WAND)
            handheld(SlabMagicItems.DESC_WAND)

            handheld(SlabMagicItems.SPELL_SWORD)

            // Block Item
            fun block(item: BlockItem) = child(item,Registry.BLOCK.getId(item.block).path)

            block(SlabMagicItems.SLAB)
            block(SlabMagicItems.REDSTONE_HEART)
            block(SlabMagicItems.CONTAMINATED_REDSTONE_HEART)
            block(SlabMagicItems.ACTIVATOR_CONCENTRATOR)
            block(SlabMagicItems.UPGRADED_ACTIVATOR_CONCENTRATOR)
            block(SlabMagicItems.ACTIVATOR_BLOCK)
            block(SlabMagicItems.SMART_ACTIVATOR_BLOCK)


            // Other

        }
    }

    fun modelChild(item: Item, parent: String) = Model(Optional.of(SlabMagicMod.id("block/" + parent)), Optional.empty())
}