package slabmagic.shape.painter.vertex

import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.texture.Sprite
import net.minecraft.util.math.Matrix4f

class ParticleVPC(val sprite: Sprite, val matrix: Matrix4f, val consumer: VertexConsumer) : VertexPainterConsumer {
    override fun add(x: Float, y: Float, z: Float, red: Float, green: Float, blue: Float, corner: Int) {
        consumer
            .vertex(matrix, x,y,z)
            .texture(if(corner==0 || corner==3) sprite.minU else sprite.maxU, if(corner<=1) sprite.minV else sprite.maxV)
            .color(red,green,blue,1f)
            .light(15728880)
            .next()
    }
}