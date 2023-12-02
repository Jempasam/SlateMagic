package slabmagic.shape.painter

import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.random.Random
import slabmagic.shape.SpellShape
import slabmagic.shape.painter.vertex.VertexPainterConsumer
import kotlin.math.sqrt

class SphereVertexPainter(val consume: VertexPainterConsumer, color: Int, val thickness: Float, var depth: Float=0.0F, val upping: Float=0.05f) : SpellShape.Painter {

    val red=ColorHelper.Argb.getRed(color)/255.0f
    val green=ColorHelper.Argb.getGreen(color)/255.0f
    val blue=ColorHelper.Argb.getBlue(color)/255.0f

    var lastX=Float.NaN
    var lastY=Float.NaN
    var random= Random.create()

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
        depth+=upping
        lastX=Float.NaN
        random=Random.create()
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
        val px1 = x1 + dx
        val py1 = y1 + dy
        val px2 = x1 - dx
        val py2 = y1 - dy
        val px3 = x2 - dx
        val py3 = y2 - dy
        val px4 = x2 + dx
        val py4 = y2 + dy

        consume.add(px1, depth+0.02f, py1, red, green, blue, 0)
        consume.add(px2, depth, py2, red, green, blue, 1)
        consume.add(px3, depth, py3, red, green, blue, 2)
        consume.add(px4, depth+0.02f, py4, red, green, blue, 3)

        consume.add(px1, depth+0.02f, py1, red, green, blue, 0)
        consume.add(px4, depth+0.02f, py4, red, green, blue, 3)
        consume.add(px3, depth, py3, red, green, blue, 2)
        consume.add(px2, depth, py2, red, green, blue, 1)

        consume.add(px1, -depth-0.02f, py1, red, green, blue, 0)
        consume.add(px2, -depth, py2, red, green, blue, 1)
        consume.add(px3, -depth, py3, red, green, blue, 2)
        consume.add(px4, -depth-0.02f, py4, red, green, blue, 3)

        consume.add(px1, -depth-0.02f, py1, red, green, blue, 0)
        consume.add(px4, -depth-0.02f, py4, red, green, blue, 3)
        consume.add(px3, -depth, py3, red, green, blue, 2)
        consume.add(px2, -depth, py2, red, green, blue, 1)
    }
}