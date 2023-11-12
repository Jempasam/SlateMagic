package slatemagic.shape.painter

import slatemagic.shape.SpellShape

class IncompletePainter(val decorated: SpellShape.Painter, val completeness: Double, val step: Int): SpellShape.Painter{

    var current=0
    var lastX=-1.0
    var lastY=-1.0

    override fun add(x: Double, y: Double) {
        if(current%step<completeness.toInt()){
            decorated.add(x,y)
            lastX=x
            lastY=y
        }
        else if(current%step==completeness.toInt()){
            if(lastX!=-1.0){
                val offset=completeness-completeness.toInt()
                val noffset=1.0-offset
                decorated.add(x*offset+lastX*noffset, y*offset+lastY*noffset)
            }
        }
        else decorated.stop()
        current++
    }

    override fun stop() {
        decorated.stop()
    }
}