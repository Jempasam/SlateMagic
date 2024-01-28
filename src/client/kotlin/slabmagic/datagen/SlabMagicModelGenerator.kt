package slabmagic.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.data.client.*
import net.minecraft.data.client.TextureKey.*
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry
import slabmagic.SlabMagicMod
import slabmagic.block.LEVEL
import slabmagic.block.SlabMagicBlocks
import slabmagic.block.TRIGGERED
import slabmagic.datagen.tools.SamModelGenerator
import slabmagic.item.SlabMagicItems
import java.util.*

class SlabMagicModelGenerator(dataGenerator: FabricDataGenerator) : FabricModelProvider(dataGenerator){


    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        val samge= SamModelGenerator(blockStateModelGenerator)
        samge.apply{
            blockStateModelGenerator.apply {
                val COPPERS= arrayOf(Blocks.COPPER_BLOCK, Blocks.EXPOSED_COPPER, Blocks.WEATHERED_COPPER, Blocks.OXIDIZED_COPPER)

                // Contaminable
                val CONTAMINATED_ID=SlabMagicMod.id("block/contaminated")
                val CONTAMINATED_CUBE_ALL=mChildOf(CONTAMINATED_ID, ALL)

                fun contaminableTriggered(block: Block, contaminated: Block){
                    registerOnBool(
                        block,
                        TRIGGERED,
                        tAll(Models.CUBE_ALL, texid(block,"_triggered")),
                        TexturedModel.CUBE_ALL
                    )
                    registerOnBool(contaminated, TRIGGERED,
                        tAll(CONTAMINATED_CUBE_ALL, texid(block,"_triggered")),
                        tAll(CONTAMINATED_CUBE_ALL, texid(block))
                    )
                }

                contaminableTriggered(SlabMagicBlocks.REDSTONE_HEART, SlabMagicBlocks.CONTAMINATED_REDSTONE_HEART)
                contaminableTriggered(SlabMagicBlocks.REDSTONE_EYE, SlabMagicBlocks.CONTAMINATED_REDSTONE_EYE)

                // Triggerable
                fun triggered(block: Block){
                    registerOnBool(
                        block,
                        TRIGGERED,
                        tAll(Models.CUBE_ALL, texid(block,"_triggered")),
                        TexturedModel.CUBE_ALL
                    )
                }

                triggered(SlabMagicBlocks.ACTIVATOR_CONCENTRATOR)
                triggered(SlabMagicBlocks.UPGRADED_ACTIVATOR_CONCENTRATOR)
                triggered(SlabMagicBlocks.CONDUCTOR)


                fun registerCopper(blocks: SlabMagicBlocks.CopperBlocks){
                    for(i in 0..<4){
                        val unwaxed=blocks.unwaxed[i]
                        val waxed=blocks.waxed[i]
                        registerSimpleCubeAll(unwaxed)
                        registerSingleton(waxed,tAll(Models.CUBE_ALL, texid(unwaxed)))
                    }
                }
                registerCopper(SlabMagicBlocks.COPPER_GRATE)
                registerCopper(SlabMagicBlocks.COPPER_WINDOW)
                registerCopper(SlabMagicBlocks.CHISELED_COPPER)

                // Pillar
                fun registerCopperPillar(blocks: SlabMagicBlocks.CopperBlocks){
                    for(i in 0..<4){
                        val copper=COPPERS[i]
                        val unwaxed=blocks.unwaxed[i]
                        val waxed=blocks.waxed[i]
                        registerPillar(unwaxed, copper)
                        registerSingleton(waxed, tmPillar(unwaxed,copper))
                    }
                }
                registerCopperPillar(SlabMagicBlocks.COPPER_BRICKS)
                registerCopperPillar(SlabMagicBlocks.METAL_SANDWICH)

                // Battery
                fun registerBattery(block: Block, topblock: Block) = registerOnProp(block, LEVEL,
                    (0..7).map{ it to t(Models.CUBE_TOP, TOP to texid(topblock), SIDE to texid(block,"_$it")) }
                )
                registerBattery(SlabMagicBlocks.COPPER_BATTERY, Blocks.COPPER_BLOCK)
                registerBattery(SlabMagicBlocks.GOLD_BATTERY, Blocks.GOLD_BLOCK)
                registerBattery(SlabMagicBlocks.IRON_BATTERY, Blocks.IRON_BLOCK)
                registerSimpleCubeAll(SlabMagicBlocks.ENERGY_BATTERY)


                // Totem
                val OVERLAY=of("overlay")
                fun registerTotem(block: Block, name: String) = registerSingleton(block,t(
                    mChildOf(SlabMagicMod/"block/totem", UP, DOWN, SIDE, OVERLAY),
                    UP to SlabMagicMod/"block/slab/$name",
                    DOWN to SlabMagicMod/"block/slab/$name",
                    SIDE to SlabMagicMod/"block/slab/$name",
                    OVERLAY to SlabMagicMod/"block/slab/${name}_overlay",
                ))

                registerTotem(SlabMagicBlocks.SLAB, "simple")
                registerTotem(SlabMagicBlocks.OLD_SLAB, "cursed")
                registerTotem(SlabMagicBlocks.ENERGY_SLAB, "magic")

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
            handheld(SlabMagicItems.ACTIVATOR_WAND)
            handheld(SlabMagicItems.ULTRA_WAND)
            handheld(SlabMagicItems.COST_WAND)
            handheld(SlabMagicItems.DESC_WAND)

            handheld(SlabMagicItems.SPELL_SWORD)
            handheld(SlabMagicItems.SPELL_WAND)
            handheld(SlabMagicItems.COMMAND_WAND)

            // Block Item
            fun block(item: BlockItem, suffix: String="") = child(item,Registry.BLOCK.getId(item.block).path+suffix)

            block(SlabMagicItems.SLAB)
            block(SlabMagicItems.OLD_SLAB)
            block(SlabMagicItems.ENERGY_SLAB)

            block(SlabMagicItems.REDSTONE_HEART)
            block(SlabMagicItems.REDSTONE_EYE)
            block(SlabMagicItems.CONTAMINATED_REDSTONE_HEART)

            block(SlabMagicItems.COPPER_BATTERY)
            block(SlabMagicItems.GOLD_BATTERY)
            block(SlabMagicItems.IRON_BATTERY)
            block(SlabMagicItems.ENERGY_BATTERY)

            block(SlabMagicItems.ACTIVATOR_CONCENTRATOR)
            block(SlabMagicItems.UPGRADED_ACTIVATOR_CONCENTRATOR)
            block(SlabMagicItems.CONDUCTOR)

            SlabMagicItems.COPPER_GRATE.forEach { block(it) }
            SlabMagicItems.COPPER_WINDOW.forEach { block(it) }
            SlabMagicItems.COPPER_BRICKS.forEach { block(it) }
            SlabMagicItems.CHISELED_COPPER.forEach { block(it) }
            SlabMagicItems.METAL_SANDWICH.forEach { block(it) }


            // Other

        }
    }

    fun modelChild(item: Item, parent: String) = Model(Optional.of(SlabMagicMod.id("block/$parent")), Optional.empty())
}