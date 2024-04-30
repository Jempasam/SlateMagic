package slabmagic.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import slabmagic.entity.tracked.provideDelegate
import java.util.*
import kotlin.math.abs

abstract class FollowingEntity(type: EntityType<*>, world: World, range: Float = 1f) : Entity(type, world) {


    companion object{
        val RANGE= DataTracker.registerData(FollowingEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val TARGET= DataTracker.registerData(FollowingEntity::class.java, TrackedDataHandlerRegistry.OPTIONAL_INT)
    }

    var target
        get() = dataTracker[TARGET]?.let { if(it.isPresent) it.asInt else null } ?.let { world.getEntityById(it) }
        set(value) = dataTracker.set(TARGET, if(value==null) OptionalInt.empty() else OptionalInt.of(value.id))

    var range by RANGE

    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(RANGE,1.0f)
        builder.add(TARGET, null)
    }

    init {
        this.range=range
    }

    override fun tick() {
        super.tick()

        setPosition(pos.x+velocity.x,pos.y+velocity.y,pos.z+velocity.z)
        velocity=velocity.multiply(0.02)
        if(abs(velocity.x) <0.01 && abs(velocity.y) <0.01 && abs(velocity.z) <0.01)velocity= Vec3d.ZERO
        velocityDirty=true
        scheduleVelocityUpdate()

        /*val world=world
        if(!world.isClient){
            val target=target
            val range=range.toDouble()
            if(target==null){
                val collisions=world.getOtherEntities(this, Box.of(pos,range,range,range)){ it.isLiving }
                this.target=collisions.minByOrNull{ it.pos.distanceTo(this.pos) }
                System.out.println("target: ${this.target?.uuid}")
                if(this.target==null) kill()
            }
            else if(target.isAlive){
                val velocity=pos(target).subtract(pos)
                setVelocity(velocity)
                velocityDirty = true
                scheduleVelocityUpdate()
                tickTarget(target)
            }
            else kill()
        }*/

        val world=world
        val target=target
        val range=range.toDouble()
        if(target==null){
            if(!world.isClient){
                val collisions=world.getOtherEntities(this, Box.of(pos,range,range,range)){ it.isLiving }
                this.target=collisions.minByOrNull{ it.pos.distanceTo(this.pos) }
                println("target: ${this.target?.uuid}")
                if(this.target==null) kill()
            }
        }
        else if(target.isAlive){
            if(!world.isClient){
                val velocity=pos(target).subtract(pos)
                setVelocity(velocity)
                velocityDirty = true
                scheduleVelocityUpdate()
            }
            tickTarget(target)
        }
        else if(!world.isClient)kill()
    }

    abstract fun tickTarget(target: Entity)

    abstract fun pos(target: Entity): Vec3d

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        range=nbt.getFloat("range")
        val world=world
        target = try { if (world is ServerWorld) nbt.getUuid("target")?.let { world.getEntity(it) } else null }
        catch(e: Exception){ null }
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        nbt.putFloat("range", range)
        target?.let{ nbt.putUuid("target", it.uuid) }
    }

    override fun createSpawnPacket() = EntitySpawnS2CPacket(this)
}