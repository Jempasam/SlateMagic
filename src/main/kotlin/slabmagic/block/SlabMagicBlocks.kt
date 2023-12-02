package slabmagic.block

import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.util.registry.Registry
import slabmagic.SlabMagicMod
import slabmagic.block.entity.SlabBlockEntity

object SlabMagicBlocks {
    fun <T: Block> create(id: String, block: T): T{
        Registry.register(Registry.BLOCK, SlabMagicMod.id(id), block)
        return block
    }

    val SLAB= create("slab", SlabBlock(::SlabBlockEntity, AbstractBlock.Settings.copy(Blocks.STONE)))
    val ACTIVATOR_BLOCK= create("activator_block", ActivatorBlock(AbstractBlock.Settings.copy(Blocks.STONE),false))
    val SMART_ACTIVATOR_BLOCK= create("smart_activator_block", ActivatorBlock(AbstractBlock.Settings.copy(Blocks.STONE),true))

    val ACTIVATOR_CONCENTRATOR= create("activator_concentrator", ConcentratorBlock(ConcentratorBlock::cast, AbstractBlock.Settings.copy(Blocks.STONE)))
    val UPGRADED_ACTIVATOR_CONCENTRATOR= create("upgraded_activator_concentrator", ConcentratorBlock(ConcentratorBlock::cast, AbstractBlock.Settings.copy(Blocks.STONE)))
}