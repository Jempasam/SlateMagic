package slabmagic.particle

import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.ParticleTextureSheet
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.render.Camera
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Matrix4f
import slabmagic.shape.painter.IncompletePartPainter
import slabmagic.shape.painter.SphereVertexPainter
import slabmagic.shape.painter.vertex.ParticleVPC
import kotlin.math.abs

class SpellSphereParticle(
    spriteProvider: SpriteProvider,
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    vx: Double,
    vy: Double,
    vz: Double,
    params: SpellCircleParticleEffect
) : Particle(world, x, y, z, vx, vy, vz) {

    init {
        maxAge=params.duration
        red=1.0f
        green=1.0f
        blue=1.0f
        velocityX = vx
        velocityY = vy
        velocityZ = vz
    }
    val shape=params.shape
    val color=params.color
    val size=params.size
    val sprite=spriteProvider.getSprite(random)

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT

    override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        val vec3d = camera.pos
        val f = (MathHelper.lerp(tickDelta.toDouble(), prevPosX, x) - vec3d.getX()).toFloat()
        val g = (MathHelper.lerp(tickDelta.toDouble(), prevPosY, y) - vec3d.getY()).toFloat()
        val h = (MathHelper.lerp(tickDelta.toDouble(), prevPosZ, z) - vec3d.getZ()).toFloat()

        val time = age.toFloat() + tickDelta
        val completness = time/maxAge
        val updown = (1.0- abs(1.0-completness*2))*150

        val matrix=Matrix4f()
        matrix.loadIdentity()
        matrix.multiply(Matrix4f.scale(size,size,size))
        //matrix.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0f))
        matrix.multiplyByTranslation(f/size,g/size,h/size)

        run {
            val painter = IncompletePartPainter(
                SphereVertexPainter(ParticleVPC(sprite, matrix, vertexConsumer), color, 0.2f, 0.0f, 0.2f),
                updown / 2.5
            )
            shape.draw(painter, time/10.0)
        }

        run{
            val painter=IncompletePartPainter(
                SphereVertexPainter(ParticleVPC(sprite, matrix, vertexConsumer), 0xffffff, 0.1f,0.01f, 0.2f),
                completness/3.5
            )
            shape.draw(painter, updown/10.0)
        }

    }

    class Factory(val sprite: SpriteProvider): ParticleFactory<SpellCircleParticleEffect>{
        override fun createParticle(effect: SpellCircleParticleEffect, world: ClientWorld, x: Double, y: Double, z: Double, vx: Double, vy: Double, vz: Double): SpellSphereParticle {
            return SpellSphereParticle(sprite, world, x, y, z, vx, vy, vz, effect)
        }
    }
}