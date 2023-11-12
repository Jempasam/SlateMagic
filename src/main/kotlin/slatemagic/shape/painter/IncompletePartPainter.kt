package slatemagic.shape.painter

import slatemagic.shape.SpellShape

class IncompletePartPainter(val decorated: SpellShape.Painter, val completeness: Double, val latency: Int=1, var group: Int=100): SpellShape.Painter{

    var start=0
    var counter=0
    var current=0
    var lastX=-1.0
    var lastY=-1.0

    override fun add(x: Double, y: Double) {
        if(current<completeness.toInt()){
            decorated.add(x,y)
            lastX=x
            lastY=y
        }
        else if(current==completeness.toInt()){
            if(lastX!=-1.0){
                val offset=completeness-completeness.toInt()
                val noffset=1.0-offset
                decorated.add(x*offset+lastX*noffset, y*offset+lastY*noffset)
            }
        }
        else{
            if(lastX!=-1.0)decorated.stop()
            lastX=-1.0
        }
        current++
    }

    override fun stop() {
        if(lastX!=-1.0)decorated.stop()
        counter++
        if(counter>=group) {
            counter = 0
            start=0
        }
        else start+=latency
        current=start
    }
}