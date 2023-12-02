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

class EnergyBlockParticle(
    spriteProvider: SpriteProvider,
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    vx: Double,
    vy: Double,
    vz: Double,
    params: EnergyBlockParticleEffect
) : Particle(world, x, y, z, vx, vy, vz) {

    init {
        maxAge=20
        red=params.colorFrom.x
        green=params.colorFrom.y
        blue=params.colorFrom.z
        alpha=1.0f
        velocityX = 0.0
        velocityY = 0.0
        velocityZ = 0.0
    }
    val fromColor=params.colorFrom
    val toColor=params.colorTo
    val size=params.size
    val sprite=spriteProvider.getSprite(random)

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT

    override fun buildGeometry(consumer: VertexConsumer, camera: Camera, tickDelta: Float) {

        val vec3d = camera.pos
        val f = (MathHelper.lerp(tickDelta.toDouble(), prevPosX, x) - vec3d.getX()).toFloat()
        val g = (MathHelper.lerp(tickDelta.toDouble(), prevPosY, y) - vec3d.getY()).toFloat()
        val h = (MathHelper.lerp(tickDelta.toDouble(), prevPosZ, z) - vec3d.getZ()).toFloat()

        val time = age.toFloat() + tickDelta

        val size= MathHelper.lerp((time+tickDelta)/maxAge, size, size*1.5f)



        val matrix=Matrix4f()
        matrix.loadIdentity()
        matrix.multiplyByTranslation(f,g,h)

        fun VertexConsumer.point(x:Float, y:Float, z:Float, corner: Int){
            vertex(matrix,x*size,y*size,z*size)
            texture(if(corner==0 || corner==3) sprite.minU else sprite.maxU, if(corner<=1) sprite.minV else sprite.maxV)
            color(red,green,blue,alpha)
            light(15728880)
            next()
        }

        fun VertexConsumer.face(x:Float, y:Float, z:Float, x2:Float, y2:Float, z2: Float, x3: Float, y3: Float, z3: Float){
            point(x, y, z, 0)
            point(x+x2, y+y2, z+z2, 1)
            point(x+x2+x3, y+y2+y3, z+z2+z3, 2)
            point(x+x3, y+y3, z+z3, 3)
        }

        consumer.apply {
            face(-1f,-1f,1f, 2f,0f,0f, 0f,2f,0f)
            face(-1f,-1f,-1f, 0f,2f,0f, 2f,0f,0f)

            face(1f,-1f,-1f, 0f,2f,0f, 0f,0f,2f)
            face(-1f,-1f,-1f, 0f,0f,2f, 0f,2f,0f)

            face(-1f,1f,-1f, 0f,0f,2f, 2f,0f,0f)
            face(-1f,-1f,-1f, 2f,0f,0f, 0f,0f,2f)
        }

    }

    override fun tick() {
        super.tick()
        val age=age/maxAge.toFloat()
        red=MathHelper.lerp(age, fromColor.x, toColor.x)
        green=MathHelper.lerp(age, fromColor.y, toColor.y)
        blue=MathHelper.lerp(age, fromColor.z, toColor.z)
        alpha=MathHelper.lerp(age, 1.0f, 0.0f)
    }

    class Factory(val sprite: SpriteProvider): ParticleFactory<EnergyBlockParticleEffect>{
        override fun createParticle(effect: EnergyBlockParticleEffect, world: ClientWorld, x: Double, y: Double, z: Double, vx: Double, vy: Double, vz: Double): EnergyBlockParticle {
            return EnergyBlockParticle(sprite, world, x, y, z, vx, vy, vz, effect)
        }
    }
}