package slatemagic.ui

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.ColorHelper
import slatemagic.SlateMagicModClient
import slatemagic.helper.ColorTools
import slatemagic.shape.painter.VertexPainter
import kotlin.math.PI

class ShowSpellGUI: HudRenderCallback {
    override fun onHudRender(matrixStack: MatrixStack, tickDelta: Float) {
        if(System.currentTimeMillis() - SlateMagicModClient.lastTime >1000*5){
            SlateMagicModClient.spell =null
        }
        val spell= SlateMagicModClient.spell
        if(spell!=null){
            val client= MinecraftClient.getInstance()
            val width=client.window.scaledWidth
            val height=client.window.scaledHeight
            val size=Math.min(height,width)/2.0f
            val x=width/2.0
            val y=height/2.0
            val rotation=System.currentTimeMillis()%5000/5000.0* PI *2

            val tesselator= Tessellator.getInstance()
            val buffer=tesselator.buffer

            MinecraftClient.getInstance().textRenderer.draw(matrixStack, spell.name, 10.0f, 10.0f, ColorTools.int(spell.color))
            MinecraftClient.getInstance().textRenderer.draw(matrixStack, spell.description, 10.0f, 20.0f, ColorTools.int(spell.color))

            RenderSystem.setShader(GameRenderer::getPositionColorShader)
            RenderSystem.disableTexture()
            RenderSystem.enableBlend()
            RenderSystem.defaultBlendFunc()

            matrixStack.push()
            matrixStack.scale(size,size,1f)
            matrixStack.translate(x/size,y/size,0.0)

            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)
            val painter=
                VertexPainter(buffer, matrixStack.peek().positionMatrix, ColorHelper.Argb.getArgb(100,255,255,255), 0.1f)
            spell.shape.draw(painter, rotation)
            val painter2= VertexPainter(buffer, matrixStack.peek().positionMatrix, ColorTools.int(spell.color), 0.05f)
            spell.shape.draw(painter2, rotation)
            tesselator.draw()

            matrixStack.pop()

            RenderSystem.disableBlend()
            RenderSystem.enableTexture()
        }
    }
}