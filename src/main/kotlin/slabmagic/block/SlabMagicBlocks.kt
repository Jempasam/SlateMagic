package slabmagic.block

import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.Oxidizable.OxidationLevel.*
import net.minecraft.block.OxidizableBlock
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.registry.Registry
import slabmagic.SlabMagicMod
import slabmagic.block.entity.PartBlockEntity
import slabmagic.helper.ColorTools
import slabmagic.item.WandItem
import slabmagic.simulator.FalseBlock

object SlabMagicBlocks {
    fun <T: Block> create(id: String, block: T): T{
        Registry.register(Registry.BLOCK, SlabMagicMod.id(id), block)
        return block
    }

    val SLAB= create("slab", SlabBlock(PartBlockEntity::factory, AbstractBlock.Settings.copy(Blocks.STONE)))
    val OLD_SLAB= create("old_slab", SlabBlock(PartBlockEntity::factory, AbstractBlock.Settings.copy(Blocks.DEEPSLATE).strength(100F, 300F)))
    val ENERGY_SLAB= create("energy_slab", ConsumableSlabBlock(PartBlockEntity::factory, AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_STAINED_GLASS)))

    val ACTIVATOR_CONCENTRATOR= create("activator_concentrator", ConcentratorBlock(WandItem.withLevel(WandItem::castAndConsume,1), AbstractBlock.Settings.copy(Blocks.STONE)))
    val UPGRADED_ACTIVATOR_CONCENTRATOR= create("upgraded_activator_concentrator", ConcentratorBlock(WandItem::castAndConsume, AbstractBlock.Settings.copy(Blocks.STONE)))

    val REDSTONE_HEART= create("redstone_heart", TickBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK),20,0))
    val CONTAMINATED_REDSTONE_HEART= create("contaminated_redstone_heart", TickBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK).strength(100F, 300F), 100, 100))

    val REDSTONE_EYE= create("redstone_eye", PlayerDetectorBlock(LivingEntity::class.java, 10.0, 80, AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)))
    val CONTAMINATED_REDSTONE_EYE= create("contaminated_redstone_eye", PlayerDetectorBlock(PlayerEntity::class.java, 50.0, 200, AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK).strength(100F, 300F)))

    val CONDUCTOR= create("conductor", ConductorBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)))

    val COPPER_BATTERY= create("copper_battery", BatteryBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK), 100, 100, ColorTools.RED))
    val GOLD_BATTERY= create("gold_battery", BatteryBlock(AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK), 200, 500, ColorTools.BLUE))
    val IRON_BATTERY= create("iron_battery", BatteryBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK), 25, 50, ColorTools.ORANGE))
    val LOW_ENERGY_BATTERY= create("low_energy_battery", ConsumableBatteryBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK), 250, ColorTools.BLUE))
    val ENERGY_BATTERY= create("energy_battery", ConsumableBatteryBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK), 500, ColorTools.BLUE))

    val FALSE_BLOCK= create("false_block",FalseBlock)

    /* COPPER */
    data class CopperBlocks(val unwaxed: Array<OxidizableBlock>, val waxed: Array<Block>): Iterable<Block>{
        override fun iterator() = (unwaxed.asSequence()+waxed.asSequence()).iterator()
    }
    fun createCoppers(id: String, settings: AbstractBlock.Settings.()->Unit): CopperBlocks{
        val ret=CopperBlocks(
            arrayOf(
                create(id, OxidizableBlock(UNAFFECTED, AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK).apply { settings() })),
                create("exposed_$id", OxidizableBlock(EXPOSED, AbstractBlock.Settings.copy(Blocks.EXPOSED_COPPER).apply { settings() })),
                create("weathered_$id", OxidizableBlock(WEATHERED, AbstractBlock.Settings.copy(Blocks.WEATHERED_COPPER).apply { settings() })),
                create("oxidised_$id", OxidizableBlock(OXIDIZED, AbstractBlock.Settings.copy(Blocks.OXIDIZED_COPPER).apply { settings() }))
            ),
            arrayOf(
                create("waxed_$id", Block(AbstractBlock.Settings.copy(Blocks.WAXED_COPPER_BLOCK).apply { settings() })),
                create("waxed_exposed_$id", Block(AbstractBlock.Settings.copy(Blocks.WAXED_EXPOSED_COPPER).apply { settings() })),
                create("waxed_weathered_$id", Block(AbstractBlock.Settings.copy(Blocks.WAXED_WEATHERED_COPPER).apply { settings() })),
                create("waxed_oxidised_$id", Block(AbstractBlock.Settings.copy(Blocks.WAXED_OXIDIZED_COPPER).apply { settings() }))
            )
        )
        for(i in 0..<3) OxidizableBlocksRegistry.registerOxidizableBlockPair(ret.unwaxed[i], ret.unwaxed[i+1])
        for(i in 0..<4) OxidizableBlocksRegistry.registerWaxableBlockPair(ret.unwaxed[i], ret.waxed[i])
        return ret
    }

    val COPPER_GRATE= createCoppers("copper_grate"){nonOpaque()}
    val COPPER_WINDOW= createCoppers("copper_window"){nonOpaque()}
    val COPPER_BRICKS= createCoppers("copper_bricks"){}
    val CHISELED_COPPER= createCoppers("chiseled_copper"){}
    val METAL_SANDWICH= createCoppers("metal_sandwich"){}
}