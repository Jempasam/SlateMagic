package slabmagic.shape.painter.vertex

import net.minecraft.client.render.VertexConsumer
import org.joml.Matrix4f

class PositionColorLightVPC(val consumer: VertexConsumer, val matrix: Matrix4f) : VertexPainterConsumer {
    override fun add(x: Float, y: Float, z: Float, red: Float, green: Float, blue: Float, corner: Int) {
        consumer.vertex(matrix,x,y,z).color(red,green,blue,1f).light(15728880).next()
    }
}