package slatemagic.block

import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.util.registry.Registry
import slatemagic.SlateMagicMod
import slatemagic.block.entity.SlateBlockEntity

object SlateMagicBlocks {
    fun <T: Block> create(id: String, block: T): T{
        Registry.register(Registry.BLOCK, SlateMagicMod.id(id), block)
        return block
    }

    val SLATE_BLOCK= create("slate_block", SlateBlock(::SlateBlockEntity, AbstractBlock.Settings.copy(net.minecraft.block.Blocks.STONE)))
}