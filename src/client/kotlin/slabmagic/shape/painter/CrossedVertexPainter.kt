package slabmagic.shape.painter

import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.random.Random
import slabmagic.shape.SpellShape
import slabmagic.shape.painter.vertex.VertexPainterConsumer
import kotlin.math.sqrt

class CrossedVertexPainter(val consumer: VertexPainterConsumer, color: Int, val thickness: Float) : SpellShape.Painter {

    val red=ColorHelper.Argb.getRed(color)/255.0f
    val green=ColorHelper.Argb.getGreen(color)/255.0f
    val blue=ColorHelper.Argb.getBlue(color)/255.0f

    var lastX=Float.NaN
    var lastY=Float.NaN
    var random= Random.create()
    var rotation=0

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
        lastX=Float.NaN
        rotation++
        if(rotation==3)rotation=0
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

        if(rotation==0){
            consumer.add(px1,0.0f,py1,red,green,blue,0)
            consumer.add(px2,0.0f,py2,red,green,blue,1)
            consumer.add(px3,0.0f,py3,red,green,blue,2)
            consumer.add(px4,0.0f,py4,red,green,blue,3)

            consumer.add(px1,0.0f,py1,red,green,blue,0)
            consumer.add(px4,0.0f,py4,red,green,blue,3)
            consumer.add(px3,0.0f,py3,red,green,blue,2)
            consumer.add(px2,0.0f,py2,red,green,blue,1)
        }
        else if(rotation==1){
            consumer.add(0.0f,px1,py1,red,green,blue,0)
            consumer.add(0.0f,px2,py2,red,green,blue,1)
            consumer.add(0.0f,px3,py3,red,green,blue,2)
            consumer.add(0.0f,px4,py4,red,green,blue,3)

            consumer.add(0.0f,px1,py1,red,green,blue,0)
            consumer.add(0.0f,px4,py4,red,green,blue,3)
            consumer.add(0.0f,px3,py3,red,green,blue,2)
            consumer.add(0.0f,px2,py2,red,green,blue,1)
        }
        else{
            consumer.add(px1,py1,0.0f,red,green,blue,0)
            consumer.add(px2,py2,0.0f,red,green,blue,1)
            consumer.add(px3,py3,0.0f,red,green,blue,2)
            consumer.add(px4,py4,0.0f,red,green,blue,3)

            consumer.add(px1,py1,0.0f,red,green,blue,0)
            consumer.add(px4,py4,0.0f,red,green,blue,3)
            consumer.add(px3,py3,0.0f,red,green,blue,2)
            consumer.add(px2,py2,0.0f,red,green,blue,1)
        }
    }
}