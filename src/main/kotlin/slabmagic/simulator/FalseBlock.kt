package slabmagic.simulator

import net.minecraft.block.Block
import net.minecraft.text.Text

object FalseBlock: Block(Settings.copy(net.minecraft.block.Blocks.STONE)) {
    override fun getName() = Text.literal("a block")
}