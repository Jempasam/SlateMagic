package slabmagic.datagen.language

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

        addDropSpellPart(SlabMagicBlocks.SLAB)

        addDrop(SlabMagicBlocks.REDSTONE_HEART)
        addDropWithSilkTouch(SlabMagicBlocks.CONTAMINATED_REDSTONE_HEART)

        addDrop(SlabMagicBlocks.ACTIVATOR_CONCENTRATOR)
        addDrop(SlabMagicBlocks.UPGRADED_ACTIVATOR_CONCENTRATOR)

    }
}