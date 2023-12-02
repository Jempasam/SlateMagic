package slabmagic.shape.painter

import slabmagic.shape.SpellShape
import java.awt.Color
import java.awt.Graphics
import kotlin.math.sqrt

class GraphicsPainter(val graphics: Graphics, val color: Color, val cx: Int, val cy: Int, val sx: Int, val sy: Int): SpellShape.Painter{

    var firstX=-1
    var firstY=-1

    var lastX=-1
    var lastY=-1

    override fun add(x: Double, y: Double) {
        val nX=(x*sx+cx).toInt()
        val nY=(y*sy+cy).toInt()
        if(lastX!=-1){
            graphics.color= Color.BLACK
            drawThickLine(lastX,lastY,nX,nY, 20)
            graphics.color= color
            drawThickLine(lastX,lastY,nX,nY,10)
            graphics.color= Color.WHITE
            drawThickLine(lastX,lastY,nX,nY,5)
        }
        else{
            firstX=nX
            firstY=nY
        }
        lastX=nX
        lastY=nY
    }

    override fun stop() {
        lastX=-1
    }

    fun drawThickLine(x1: Int, y1: Int, x2: Int, y2: Int, thickness: Int) {
        val dX = x2 - x1
        val dY = y2 - y1
        val lineLength = sqrt(1.0 * dX * dX + dY * dY).toInt()
        val scale = (thickness / 2.0) / lineLength
        val ddx = -dY * scale
        val ddy = dX * scale
        val dx = ddx.toInt()
        val dy = ddy.toInt()
        val px1 = x1 + dx
        val py1 = y1 + dy
        val px2 = x1 - dx
        val py2 = y1 - dy
        val px3 = x2 + dx
        val py3 = y2 + dy
        val px4 = x2 - dx
        val py4 = y2 - dy
        graphics.fillPolygon(intArrayOf(px1, px2, px4, px3), intArrayOf(py1, py2, py4, py3), 4)
    }
}