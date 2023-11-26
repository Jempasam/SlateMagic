package slatemagic.entity

import net.minecraft.command.argument.EntityAnchorArgumentType
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import slatemagic.network.messages.AdvancedParticleMessage
import slatemagic.network.messages.sendParticleEffect
import slatemagic.particle.AdvancedParticle
import slatemagic.particle.MagicParticleEffect
import slatemagic.spell.SpellContext
import slatemagic.spell.build.AssembledSpell
import kotlin.math.min
import kotlin.random.Random

class SpellTrapEntity : SimpleSpellEntity{


    companion object{
        val RANGE= DataTracker.registerData(SpellTrapEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
    }

    private var remainingshoot=1
    private var time=0

    var range: Float
        get() = dataTracker.get(RANGE)
        set(value) = dataTracker.set(RANGE, value)

    constructor(type: EntityType<*>, world: World) : super(type, world)

    constructor(type: EntityType<*>, world: World, spell: AssembledSpell, power: Int, range: Float, remainingShoot: Int)
            : super(type, world, spell, power)
    {
        this.range = range
        this.remainingshoot = remainingShoot
    }

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(RANGE, 1.0f)
    }


    override fun tick() {
        super.tick()

        val world=world
        if(!world.isClient){
            world as ServerWorld
            if(time>10){
                time=0
                val range=range.toDouble()
                val collisions=world.getOtherEntities(this, Box.of(pos,range,range,range)){ it.isLiving }
                if(collisions.size>0){
                    val i = Random.nextInt(collisions.size)
                    val target=collisions[i]
                    lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, target.pos)
                    val context= SpellContext.at(this, power)
                    context.markeds.addAll(markeds)
                    context.direction= Vec2f(yaw, pitch)
                    val result=spell.use(context)
                    if(result!=null){
                        remainingshoot--
                        sendParticleEffect(world,
                            MagicParticleEffect(spell.color, 0.5f),
                            pos,
                            AdvancedParticleMessage.LIGHTNING,
                            target.pos,
                            5.0
                        )
                        sendParticleEffect(context.world,
                            MagicParticleEffect(spell.color, 0.5f),
                            context.pos,
                            AdvancedParticleMessage.SHOCKWAVE,
                            Vec3d(range,range,range),
                            15.0*range
                        )
                        if(remainingshoot<=0)kill()
                    }
                }
            }
            time++
        }
        else{
            if(time>10) {
                time = 0
                val halfrange = range / 2.0
                AdvancedParticle.cloud(
                    world,
                    MagicParticleEffect(spell.color, 0.2f),
                    pos,
                    Vec3d(halfrange, halfrange, halfrange),
                    Vec3d.ZERO,
                    min((range * range * range / 20.0).toInt(),1)
                )
            }
            time++
        }
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        range=nbt.getFloat("range")
        time=nbt.getInt("time")
        remainingshoot=nbt.getInt("remaining_shoot")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putFloat("range", range)
        nbt.putInt("time", time)
        nbt.putInt("remaining_shoot", remainingshoot)
    }
}