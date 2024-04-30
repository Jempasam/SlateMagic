package slabmagic.shape.painter.vertex

import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumer
import org.joml.Matrix4f

class EntityVPC(val consumer: VertexConsumer, val matrix: Matrix4f) : VertexPainterConsumer {

    override fun add(x: Float, y: Float, z: Float, red: Float, green: Float, blue: Float, corner: Int) {
        consumer
            .vertex(matrix,x,y,z)
            .color(red,green,blue,1f)
            .texture(if(corner==0 || corner==3) 0f else 1f, if(corner<=1) 0f else 1f)
            .overlay(OverlayTexture.DEFAULT_UV)
            .light(15728880)
            .normal(1f, 1f, 1f)
            .next()
    }

}