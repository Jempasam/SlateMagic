package slabmagic.particle

import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider
import net.minecraft.client.particle.AbstractDustParticle
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.world.ClientWorld
import kotlin.math.min

class MagicParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    vx: Double,
    vy: Double,
    vz: Double,
    params: MagicParticleEffect,
    sprites: SpriteProvider
) : AbstractDustParticle<MagicParticleEffect>(world, x, y, z, vx, vy, vz, params, sprites) {

    init {
        val light=random.nextFloat()/5
        red = min(1f,params.color.x + random.nextFloat()/5 + light)
        green = min(1f,params.color.y + random.nextFloat()/5 + light)
        blue = min(1f,params.color.z + random.nextFloat()/5 + light)
        scale = params.scale*(0.1f+random.nextFloat())/3f
        angle = random.nextFloat()*360
        setSprite(sprites.getSprite(random))
        maxAge = (10.0F+random.nextFloat()*scale*300).toInt()

        velocityX = vx
        velocityY = vy
        velocityZ = vz
    }

    class Factory(val sprites: FabricSpriteProvider): ParticleFactory<MagicParticleEffect>{
        override fun createParticle(effect: MagicParticleEffect, world: ClientWorld, x: Double, y: Double, z: Double, vx: Double, vy: Double, vz: Double): Particle {
            return MagicParticle(world, x, y, z, vx, vy, vz, effect, sprites)
        }
    }

    override fun setSpriteForAge(spriteProvider: SpriteProvider?) {
    }

    override fun tick() {
        super.tick()
        prevAngle=angle
        angle += if(maxAge-age<20){
            scale((maxAge-age)/20f)
            //angle+=1.1f-(maxAge-age)/20f
            0.1f
        } else{
            0.1f
        }
    }
}