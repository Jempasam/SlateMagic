package slabmagic.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import slabmagic.network.messages.AdvancedParticleMessage
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.MagicParticleEffect
import slabmagic.spell.SpellContext
import slabmagic.spell.build.parts.AssembledSpell

class SpellCurseEntity : SimpleSpellEntity{


    companion object{
        val RANGE= DataTracker.registerData(SpellCurseEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val TARGET= DataTracker.registerData(SpellCurseEntity::class.java, TrackedDataHandlerRegistry.OPTIONAL_UUID)
    }

    private var remainingshoot=1
    private var time=0
    private var cadency=0
    private var target: Entity? = null

    var range: Float
        get() = dataTracker.get(RANGE)
        set(value) = dataTracker.set(RANGE, value)

    constructor(type: EntityType<*>, world: World) : super(type, world)

    constructor(type: EntityType<*>, world: World, spell: AssembledSpell, power: Int, range: Float, remainingShoot: Int, cadency: Int)
            : super(type, world, spell, power)
    {
        this.range = range
        this.remainingshoot = remainingShoot
        this.cadency = cadency
    }

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(RANGE, 1.0f)
    }


    override fun tick() {
        super.tick()
        val world=world
        if(!world.isClient && remainingshoot<=0)kill()

        val target=target
        val range=range.toDouble()
        if(target==null){
            val collisions=world.getOtherEntities(this, Box.of(pos,range,range,range)){ it.isLiving }
            if(collisions.size>0){
                this.target=collisions[0]
            }
            else if(!world.isClient)kill()
        }
        else if(target.isAlive){
            setPosition(target.pos.add(0.0,0.1,0.0))
            if(!world.isClient){
                time++
                if(time>cadency){
                    val context=SpellContext.at(target,power)
                    context.markeds.addAll(markeds)
                    spell.use(context)
                    sendParticleEffect(context.world,
                        MagicParticleEffect(spell.color, 0.5f),
                        context.pos,
                        AdvancedParticleMessage.SHOCKWAVE,
                        Vec3d(range,0.0,range),
                        15.0*range
                    )
                    time=0
                    remainingshoot--
                    if(remainingshoot<=0)kill()
                }
            }
        }
        else this.target=null
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        range=nbt.getFloat("range")
        time=nbt.getInt("time")
        remainingshoot=nbt.getInt("remaining_shoot")
        cadency=nbt.getInt("cadency")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putFloat("range", range)
        nbt.putInt("time", time)
        nbt.putInt("remaining_shoot", remainingshoot)
        nbt.putInt("cadency", cadency)
    }
}