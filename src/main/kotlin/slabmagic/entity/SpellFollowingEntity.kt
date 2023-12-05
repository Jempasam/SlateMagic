package slabmagic.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import slabmagic.spell.build.parts.AssembledSpell
import kotlin.math.abs

abstract class SpellFollowingEntity : SimpleSpellEntity{


    companion object{
        val RANGE= DataTracker.registerData(SpellFollowingEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
    }

    var target: Entity? = null

    var range: Float
        get() = dataTracker.get(RANGE)
        set(value) = dataTracker.set(RANGE, value)

    constructor(type: EntityType<*>, world: World) : super(type, world)

    constructor(type: EntityType<*>, world: World, spell: AssembledSpell, power: Int, range: Float)
            : super(type, world, spell, power)
    {
        this.range = range
    }

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(RANGE, 1.0f)
    }


    override fun tick() {
        super.tick()
        val world=world
        setPosition(pos.x+velocity.x,pos.y+velocity.y,pos.z+velocity.z)
        velocity=velocity.multiply(0.5)
        velocityDirty=true
        scheduleVelocityUpdate()
        if(abs(velocity.x) <0.01 && abs(velocity.y)<0.01 && abs(velocity.z)<0.01)velocity=Vec3d.ZERO
        if(!world.isClient){
            val target=target
            val range=range.toDouble()
            if(target==null){
                val collisions=world.getOtherEntities(this, Box.of(pos,range,range,range)){ it.isLiving }
                this.target=collisions.minByOrNull{ it.pos.distanceTo(this.pos) }
                if(this.target==null) kill()
            }
            else if(target.isAlive){
                val velocity=pos(target).subtract(pos)
                setVelocity(velocity)
                velocityDirty = true
                scheduleVelocityUpdate()
                tickTarget(target)
            }
            else this.target=null
        }
    }

    abstract fun tickTarget(target: Entity)

    abstract fun pos(target: Entity): Vec3d

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        range=nbt.getFloat("range")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putFloat("range", range)
    }
}