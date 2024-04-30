package slabmagic.ui

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import slabmagic.block.SlabMagicBlocks
import slabmagic.registry.SlabMagicTags

object GlassesHudRenderCallback: HudRenderCallback {

    private val providers= mutableMapOf<Block,MutableList<Provider>>()
    private var targetedPos= BlockPos.ZERO
    private var targetedBlock= Blocks.AIR
    private var targetTime= 0L
    private var menu: List<Part> = listOf()

    fun register(block: Block, provider: Provider){
        providers.computeIfAbsent(block){ mutableListOf() }.add(provider)
    }

    override fun onHudRender(context: DrawContext, tickDelta: Float) {
        val mc=MinecraftClient.getInstance()
        val player=mc.player ?: return
        val mainHand=player.mainHandStack
        val offHand=player.offHandStack
        if(mainHand.isIn(SlabMagicTags.LENS) || offHand.isIn(SlabMagicTags.LENS)){
            val looked= player.raycast(6.0, tickDelta, false)
            val bpos=BlockPos.ofFloored(looked.pos.add(player.rotationVector.multiply(0.1)))
            val state= player.world.getBlockState(bpos)
            val time=System.currentTimeMillis()
            if(state.isAir)return

            // Update Menu
            if(bpos!=targetedPos || state.block!=targetedBlock || time-targetTime>1000){
                targetedPos=bpos
                targetedBlock=state.block
                targetTime=time
                val newmenu= mutableListOf<Part>()
                val block= state.block
                val provider= providers[block]
                if(provider!=null){
                    for(p in provider){
                        newmenu.addAll(p.get(state,bpos))
                    }
                }
                menu=newmenu
            }

            // Show Menu
            val starty=(mc.window.scaledHeight*0.5f).toInt()
            var y=starty
            val x=(mc.window.scaledWidth*0.3f).toInt()
            for(part in menu){
                val dx=if(part.stack.isEmpty){
                    x
                }
                else{
                    context.drawItem(part.stack, x, y-4)
                    x+20
                }
                context.drawText(mc.textRenderer, part.text, dx, y, DyeColor.WHITE.fireworkColor, true)
                y+=20
            }

        }
    }

    init{
        register(SlabMagicBlocks.SLAB, SlabHudProvider)
        register(SlabMagicBlocks.OLD_SLAB, SlabHudProvider)
        register(SlabMagicBlocks.COPPER_BATTERY, BatteryHudProvider)
        register(SlabMagicBlocks.GOLD_BATTERY, BatteryHudProvider)
        register(SlabMagicBlocks.IRON_BATTERY, BatteryHudProvider)
    }

    data class Part(val stack: ItemStack, val text: Text)

    fun interface Provider{
        fun get(state: BlockState, pos: BlockPos): List<Part>
    }
}