package slabmagic.particle

import net.minecraft.client.util.math.Vector3d
import net.minecraft.particle.ParticleEffect
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


/**
 * Helper object for advanced particle effects
 * @author Jempasam
 */
object AdvancedParticle {


    /**
     * Spawn a particle at a position
     * @param world The world to spawn the particle in
     * @param effect The particle effect to spawn
     * @param position The position to spawn the particle at
     * @param velocity The velocity of the particle
     */
    fun point(world: World, effect: ParticleEffect, position: Vec3d, velocity: Vec3d){
        world.addParticle(effect, position.x, position.y, position.z, velocity.x, velocity.y, velocity.z)
    }

    /**
     * Spawn a line of particles from one position to another
     * @param world The world to spawn the particles in
     * @param effect The particle effect to spawn
     * @param from The position to start the line at
     * @param to The position to end the line at
     * @param velocity The velocity of the particles
     * @param spreading The maximum distance from the line to spawn particles at
     */
    fun line(world: World, effect: ParticleEffect, from: Vec3d, to: Vec3d, velocity: Vec3d, spreading: Double){
        val distance=from.distanceTo(to)*2
        var actual= Vector3d(from.x, from.y, from.z)
        for(i in 0..distance.toInt()){
            var pos=from.lerp(to, i.toDouble()/distance.toInt())
            if(spreading>0.0){
                pos=pos.add(
                    Vec3d(
                    Random.nextDouble(spreading)-spreading/2,
                    Random.nextDouble(spreading)-spreading/2,
                    Random.nextDouble(spreading)-spreading/2
                )
                )
            }
            world.addParticle(effect, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z)
        }
    }

    /**
     * Spawn a lightning effect from one position to another
     * @param world The world to spawn the particles in
     * @param effect The particle effect to spawn
     * @param from The position to start the lightning at
     * @param to The position to end the lightning at
     * @param velocity The velocity of the particles
     * @param bendCount The number of times the lightning should bend
     */
    fun lightning(world: World, effect: ParticleEffect, from: Vec3d, to: Vec3d, velocity: Vec3d, bendCount: Int)
    {
        var previous=from
        for(i in 1..bendCount){
            val to=from.lerp(to,i.toDouble()/bendCount).let {
                if(i==bendCount) it
                else Vec3d(
                    it.x+ Random.nextDouble(3.0)-1.5,
                    it.y+ Random.nextDouble(3.0)-1.5,
                    it.z+ Random.nextDouble(3.0)-1.5
                )
            }
            line(world, effect, previous, to, velocity, 0.0)
            previous=to
        }
    }

    /**
     * Spawn a curved line of particle
     * @param world The world to spawn the particles in
     * @param effect The particle effect to spawn
     * @param from The position to start the line at
     * @param to the position to end the line at
     * @param velocity The velocity of the particles
     * @param curvature The maximum strength of the curvature
     */
    fun curve(world: World, effect: ParticleEffect, from: Vec3d, to: Vec3d, velocity: Vec3d, curvature: Double)
    {
        val offset=Vec3d(
            Random.nextDouble(curvature)-curvature/2,
            Random.nextDouble(curvature)-curvature/2,
            Random.nextDouble(curvature)-curvature/2,
        )
        val distance=from.distanceTo(to).toInt()*2
        var actual= Vector3d(from.x, from.y, from.z)
        for(i in 0..distance){
            val advancement=i.toDouble()/distance
            val updown= (1-abs(advancement*2f-1f)-1).let { 1-it*it }
            var pos=from.lerp(to, advancement)
            pos=pos.add(
                Vec3d(
                    offset.x*updown,
                    offset.y*updown,
                    offset.z*updown
                )
            )
            world.addParticle(effect, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z)
        }
    }

    /**
     * Spawn a box of particle
     * @param world The world to spawn the particles in
     * @param effect The particle effect to spawn
     * @param center The center of the box
     * @param size The size of the box
     * @param velocity The velocity of the particles
     * @param count The number of particles to spawn
     */
    fun box(world: World, effect: ParticleEffect, center: Vec3d, size: Vec3d, velocity: Vec3d, count: Int)
    {
        for(i in 0..<count){
            val pos= Vec3d(
                center.x+ Random.nextDouble(size.x*2)-size.x,
                center.y+ Random.nextDouble(size.y*2)-size.y,
                center.z+ Random.nextDouble(size.z*2)-size.z
            )
            world.addParticle(effect, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z)
        }
    }

    /**
     * Spawn a cloud of particle.
     * It look like a box of particle but the distribution of particles position is more natural
     * @param world The world to spawn the particles in
     * @param effect The particle effect to spawn
     * @param center The center of the cloud
     * @param size The size of the cloud
     * @param velocity The velocity of the particles
     * @param count The number of particles to spawn
     */
    fun cloud(world: World, effect: ParticleEffect, center: Vec3d, size: Vec3d, velocity: Vec3d, count: Int)
    {
        for(i in 0..<count){
            val pos= Vec3d(
                center.x+ Random.nextDouble(size.x)+ Random.nextDouble(size.x)-size.x,
                center.y+ Random.nextDouble(size.y)+ Random.nextDouble(size.y)-size.y,
                center.z+ Random.nextDouble(size.z)+ Random.nextDouble(size.z)-size.z
            )
            world.addParticle(effect, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z)
        }
    }

    /**
     * Spawn an implosive cloud of particles, it look like a cloud of particle but the particles are attracted to the center
     * @param world The world to spawn the particles in
     * @param effect The particle effect to spawn
     * @param center The center of the cloud
     * @param size The size of the cloud
     * @param velocity The velocity of the particles
     * @param count The number of particles to spawn
     */
    fun implode(world: World, effect: ParticleEffect, center: Vec3d, size: Vec3d, velocity: Vec3d, count: Int)
    {
        for(i in 0..<count){
            val pos= Vec3d(
                center.x+ Random.nextDouble(size.x)+ Random.nextDouble(size.x)-size.x,
                center.y+ Random.nextDouble(size.y)+ Random.nextDouble(size.y)-size.y,
                center.z+ Random.nextDouble(size.z)+ Random.nextDouble(size.z)-size.z
            )
            val speed= Vec3d(
                velocity.x+(center.x-pos.x)/10f,
                velocity.y+(center.y-pos.y)/10f,
                velocity.z+(center.z-pos.z)/10f
            )
            world.addParticle(effect, pos.x, pos.y, pos.z, speed.x, speed.y, speed.z)
        }
    }

    /**
     * Spawn a spiral of particle, it look like a cloud of particle but the particles have a circular motion
     * @param world The world to spawn the particles in
     * @param effect The particle effect to spawn
     * @param center The center of the spiral
     * @param size The size of the spiral
     * @param velocity The velocity of the particles
     * @param count The number of particles to spawn
     */
    fun spiral(world: World, effect: ParticleEffect, center: Vec3d, size: Vec3d, velocity: Vec3d, count: Int)
    {
        for(i in 0..<count){
            val rand= Vec3d(
                Random.nextDouble(size.x)+ Random.nextDouble(size.x)-size.x,
                Random.nextDouble(size.y)+ Random.nextDouble(size.y)-size.y,
                Random.nextDouble(size.z)+ Random.nextDouble(size.z)-size.z
            )

            val pos= Vec3d(
                center.x + rand.x,
                center.y + rand.y,
                center.z + rand.z
            )

            val speed= Vec3d(
                velocity.x-rand.z/10f,
                velocity.y,
                velocity.z+rand.x/10f
            )
            world.addParticle(effect, pos.x, pos.y, pos.z, speed.x, speed.y, speed.z)
        }
    }

    /**
     * Spawn an explosion of particle, the particles spawn at the center and go in all direction.
     * It looks like a box of particle growing in size
     * @param world The world to spawn the particles in
     * @param effect The particle effect to spawn
     * @param center The center of the explosion
     * @param speed The maximum speed of the particles random motion
     * @param velocity The velocity of the particles, added to the random motion
     * @param count The number of particles to spawn
     */
    fun boom(world: World, effect: ParticleEffect, center: Vec3d, speed: Vec3d, velocity: Vec3d, count: Int)
    {
        for(i in 0..<count){
            val finalSpeed= Vec3d(
                velocity.x+ Random.nextDouble(speed.x*2)-speed.x,
                velocity.y+ Random.nextDouble(speed.y*2)-speed.y,
                velocity.z+ Random.nextDouble(speed.z*2)-speed.z
            )
            world.addParticle(effect, center.x, center.y, center.z, finalSpeed.x, finalSpeed.y, finalSpeed.z)
        }
    }

    /**
     * Spawn an explosion of particle, the particles spawn at the center and go in all direction.
     * It looks more natural than the boom effect
     * @param world The world to spawn the particles in
     * @param effect The particle effect to spawn
     * @param center The center of the explosion
     * @param speed The maximum speed of the particles random motion
     * @param velocity The velocity of the particles, added to the random motion
     * @param count The number of particles to spawn
     */
    fun cloudboom(world: World, effect: ParticleEffect, center: Vec3d, speed: Vec3d, velocity: Vec3d, count: Int)
    {
        for(i in 0..<count){
            val speed= Vec3d(
                velocity.x+ Random.nextDouble(speed.x)+ Random.nextDouble(speed.x)-speed.x,
                velocity.y+ Random.nextDouble(speed.y)+ Random.nextDouble(speed.y)-speed.y,
                velocity.z+ Random.nextDouble(speed.z)+ Random.nextDouble(speed.z)-speed.z
            )
            world.addParticle(effect, center.x, center.y, center.z, speed.x, speed.y, speed.z)
        }
    }

    /**
     * Spawn a storm of particles in a box.
     * It looks like a box of particle but the particles have a random motion
     * @param world The world to spawn the particles in
     * @param effect The particle effect to spawn
     * @param center The center of the storm
     * @param size The size of the storm and speed of the random motion
     * @param velocity The velocity of the particles, added to the random motion
     */
    fun storm(world: World, effect: ParticleEffect, center: Vec3d, size: Vec3d, velocity: Vec3d, count: Int){
        for(i in 0..<count){
            val pos= Vec3d(
                center.x+ Random.nextDouble(size.x*2)-size.x,
                center.y+ Random.nextDouble(size.y*2)-size.y,
                center.z+ Random.nextDouble(size.z*2)-size.z
            )
            val speed= Vec3d(
                velocity.x+ Random.nextDouble(size.x/5)-size.x/10,
                velocity.y+ Random.nextDouble(size.y/5)-size.y/10,
                velocity.z+ Random.nextDouble(size.z/5)-size.z/10
            )
            world.addParticle(effect, pos.x, pos.y, pos.z, speed.x, speed.y, speed.z)
        }
    }

    /**
     * Spawn a tornado of particles in a box.
     * @param world The world to spawn the particles in
     * @param effect The particle effect to spawn
     * @param center The center of the tornado
     * @param size The size of the tornado
     * @param velocity The velocity of the particles added to the circular motion
     * @param count The number of particles to spawn
     */
    fun tornado(world: World, effect: ParticleEffect, center: Vec3d, size: Vec3d, velocity: Vec3d, count: Int) {
        val direction=if(Random.nextBoolean()) -1 else 1
        val anglePerParticle=((Math.PI*2*count/30)/count)*direction
        val xPerParticle=size.x/count
        val yPerParticle=size.y/count
        val zPerParticle=size.z/count

        val startAngle=Random.nextFloat()*Math.PI*2
        for (i in 1 .. count) {
            val angle = startAngle + anglePerParticle*i
            val cos= cos(angle)
            val sin= sin(angle)
            val pos = Vec3d(
                center.x + xPerParticle * i * cos,
                center.y + yPerParticle * i,
                center.z + zPerParticle * i * sin
            )
            val speed = Vec3d(
                velocity.x + xPerParticle * i / 5 * sin*direction,
                velocity.y + yPerParticle * i / 10,
                velocity.z + zPerParticle * i / 5 * -cos*direction
            )
            world.addParticle(effect, pos.x, pos.y, pos.z, speed.x, speed.y, speed.z)
        }
    }

    /**
     * Spawn a ring of particles in a circular pattern.
     * @param world The world to spawn the particles in
     * @param effect The particle effect to spawn
     * @param center The center of the ring
     * @param size The size of the ring
     * @param velocity The velocity of the particles added to the circular motion
     * @param count The number of particles to spawn
     */
    fun ring(world: World, effect: ParticleEffect, center: Vec3d, size: Vec3d, velocity: Vec3d, count: Int) {
        val anglePerParticle = Math.PI * 2 / count
        val startAngle=Random.nextFloat()*Math.PI*2

        for (i in 0 until count) {
            val angle = startAngle+anglePerParticle * i
            val cos = cos(angle)
            val sin = sin(angle)
            val pos = Vec3d(
                center.x + size.x * cos,
                center.y + size.y * sin(angle),
                center.z + size.z * sin
            )
            world.addParticle(effect, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z)
        }
    }

    /**
     * Spawn a ring of particles in a circular expanding pattern.
     * @param world The world to spawn the particles in
     * @param effect The particle effect to spawn
     * @param center The center of the ring
     * @param size The size of the ring and the speed of the expansion
     * @param velocity The velocity of the particles added to the circular motion
     * @param count The number of particles to spawn
     */
    fun shockwave(world: World, effect: ParticleEffect, center: Vec3d, size: Vec3d, velocity: Vec3d, count: Int) {
        val anglePerParticle = Math.PI * 2 / count
        val startAngle=Random.nextFloat()*Math.PI*2

        for (i in 0 until count) {
            val angle = startAngle+anglePerParticle * i
            val cos = cos(angle)
            val sin = sin(angle)
            val pos = Vec3d(
                center.x + size.x * cos,
                center.y + size.y * sin,
                center.z + size.z * sin
            )
            val speed = Vec3d(
                velocity.x + size.x * cos / 10,
                velocity.y + size.y * sin / 10,
                velocity.z + size.z * sin / 10
            )
            world.addParticle(effect, pos.x, pos.y, pos.z, speed.x, speed.y, speed.z)
        }
    }
}