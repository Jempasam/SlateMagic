package slabmagic.shape.spring

import slabmagic.shape.SpellShape
import slabmagic.shape.painter.GraphicsPainter
import java.awt.Color
import java.awt.Graphics
import javax.swing.JPanel

class SpellShapeFrame(val shape: SpellShape, val color: Color): JPanel(){

    override fun paint(g: Graphics) {
        super.paint(g)
        val b=g.clipBounds
        g.color= Color.BLACK
        g.fillRect(0,0,b.width,b.height)
        val drawer= GraphicsPainter(g,
            color,
            b.width/2-b.height*3/8, b.height/2-b.height*3/8,
            b.height*3/4, b.height*3/4
        )
        shape.draw(drawer, System.currentTimeMillis()/1000.0)
    }

}