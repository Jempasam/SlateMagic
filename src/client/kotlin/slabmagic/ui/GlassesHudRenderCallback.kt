package slabmagic.ui

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
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
    private var menu: List<Part> = listOf()

    fun register(block: Block, provider: Provider){
        providers.computeIfAbsent(block){ mutableListOf() }.add(provider)
    }

    override fun onHudRender(matrixStack: MatrixStack, tickDelta: Float) {
        val mc=MinecraftClient.getInstance()
        val player=mc.player ?: return
        val mainHand=player.mainHandStack
        val offHand=player.offHandStack
        if(mainHand.isIn(SlabMagicTags.LENS) || offHand.isIn(SlabMagicTags.LENS)){
            val looked= player.raycast(6.0, tickDelta, false)
            val bpos=BlockPos(looked.pos.add(player.rotationVector.multiply(0.1)))
            val state= player.world.getBlockState(bpos)
            if(state.isAir)return

            // Update Menu
            if(bpos!=targetedPos || state.block!=targetedBlock){
                targetedPos=bpos
                targetedBlock=state.block
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
                    mc.itemRenderer.renderInGuiWithOverrides(part.stack, x, y )
                    x+20
                }
                mc.textRenderer.drawWithShadow(matrixStack, part.text, dx.toFloat(), y.toFloat()+4f, DyeColor.WHITE.fireworkColor)
                y+=20
            }

        }
    }

    init{
        register(SlabMagicBlocks.SLAB, GlassesHudProvider)
    }

    data class Part(val stack: ItemStack, val text: Text)

    fun interface Provider{
        fun get(state: BlockState, pos: BlockPos): List<Part>
    }
}