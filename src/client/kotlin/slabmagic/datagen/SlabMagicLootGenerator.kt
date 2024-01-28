package slabmagic.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.block.Block
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.function.CopyNbtLootFunction
import net.minecraft.loot.provider.nbt.ContextLootNbtProvider
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import slabmagic.block.SlabMagicBlocks

class SlabMagicLootGenerator(gen: FabricDataGenerator) : FabricBlockLootTableProvider(gen) {
    override fun generateBlockLootTables() {
        // Node Block Entity
        fun addDropSpellPart(block: Block){
            addDrop(block, LootTable.builder()
                .pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1f))
                    .with(ItemEntry.builder(block)
                        .apply(CopyNbtLootFunction.builder(ContextLootNbtProvider.BLOCK_ENTITY)
                            .withOperation("node","BlockEntityTag.node")
                        )
                    )
                )
            )
        }

        // Magic
        addDropSpellPart(SlabMagicBlocks.SLAB)

        // Machines
        addDrop(SlabMagicBlocks.REDSTONE_HEART)
        addDropWithSilkTouch(SlabMagicBlocks.CONTAMINATED_REDSTONE_HEART)

        addDrop(SlabMagicBlocks.REDSTONE_EYE)
        addDropWithSilkTouch(SlabMagicBlocks.CONTAMINATED_REDSTONE_EYE)

        addDrop(SlabMagicBlocks.ACTIVATOR_CONCENTRATOR)
        addDrop(SlabMagicBlocks.UPGRADED_ACTIVATOR_CONCENTRATOR)

        addDrop(SlabMagicBlocks.COPPER_BATTERY)
        addDrop(SlabMagicBlocks.GOLD_BATTERY)
        addDrop(SlabMagicBlocks.IRON_BATTERY)
        addDrop(SlabMagicBlocks.CONDUCTOR)

        // Building Blocks
        SlabMagicBlocks.COPPER_GRATE.forEach { addDrop(it) }
        SlabMagicBlocks.COPPER_BRICKS.forEach { addDrop(it) }
        SlabMagicBlocks.CHISELED_COPPER.forEach { addDrop(it) }
        SlabMagicBlocks.COPPER_WINDOW.forEach { addDrop(it) }
        SlabMagicBlocks.METAL_SANDWICH.forEach { addDrop(it) }
    }
}