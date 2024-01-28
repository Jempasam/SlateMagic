package slabmagic.ui

import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.item.Items
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import slabmagic.block.BatteryBlock
import slabmagic.block.LEVEL

object BatteryHudProvider: GlassesHudRenderCallback.Provider {
    override fun get(state: BlockState, pos: BlockPos): List<GlassesHudRenderCallback.Part> {
        val state= MinecraftClient.getInstance().world?.getBlockState(pos)
        val block= state?.block
        return if(state!=null && block is BatteryBlock) {
            val level=state.get(LEVEL)
            listOf(GlassesHudRenderCallback.Part(
                Items.GLOWSTONE_DUST.defaultStack,
                Text.of("$level/7 = ${level*block.power}")
            ))
        }
        else{
            listOf()
        }
    }
}