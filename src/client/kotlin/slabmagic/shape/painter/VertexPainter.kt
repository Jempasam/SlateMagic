package slabmagic.shape.painter

import net.minecraft.client.render.VertexConsumer
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.Matrix4f
import slabmagic.shape.SpellShape
import kotlin.math.sqrt

class VertexPainter(val model: VertexConsumer, val matrix: Matrix4f, color: Int, val thickness: Float) : SpellShape.Painter {

    val red=ColorHelper.Argb.getRed(color)/255.0f
    val green=ColorHelper.Argb.getGreen(color)/255.0f
    val blue=ColorHelper.Argb.getBlue(color)/255.0f
    val alpha=ColorHelper.Argb.getAlpha(color)/255.0f
    var depth: Float=0.0f

    var lastX=Float.NaN
    var lastY=Float.NaN

    override fun add(xx: Double, yy: Double) {
        val x=xx.toFloat()
        val y=yy.toFloat()
        if(!lastX.isNaN()){
            drawThickLine(lastX,lastY,x,y,thickness)
        }
        lastX=x
        lastY=y
    }

    override fun stop() {
        depth+=0.1f
        lastX=Float.NaN
    }

    private fun drawThickLine(x1: Float, y1: Float, x2: Float, y2: Float, thickness: Float) {
        val dX = x2 - x1
        val dY = y2 - y1
        val lineLength = sqrt(1.0f * dX * dX + dY * dY)
        val scale = (thickness / 2.0f) / lineLength
        val ddx = -dY * scale
        val ddy = dX * scale
        val dx = ddx
        val dy = ddy
        val px1 = x1 - dx
        val py1 = y1 - dy
        val px2 = x1 + dx
        val py2 = y1 + dy
        val px3 = x2 + dx
        val py3 = y2 + dy
        val px4 = x2 - dx
        val py4 = y2 - dy
        model.vertex(matrix,px1,py1,depth).color(red,green,blue,alpha).next()
        model.vertex(matrix,px2,py2,depth).color(red,green,blue,alpha).next()
        model.vertex(matrix,px3,py3,depth).color(red,green,blue,alpha).next()
        model.vertex(matrix,px4,py4,depth).color(red,green,blue,alpha).next()
    }
}