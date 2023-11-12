package slatemagic.entity

import net.minecraft.entity.EntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import slatemagic.network.messages.AdvancedParticleMessage
import slatemagic.network.messages.sendParticleEffect
import slatemagic.particle.MagicParticleEffect
import slatemagic.spell.SpellContext
import slatemagic.spell.effect.SpellEffect

class SpellTurretEntity : SimpleSpellEntity{


    private var cadency=0
    private var time=0
    private var remainingShoot=5


    constructor(type: EntityType<*>, world: World) : super(type, world)

    constructor(type: EntityType<*>, world: World, spell: SpellEffect, power: Int, cadency: Int, remainingShoot: Int)
            : super(type, world, spell, power)
    {
        this.cadency = cadency
        this.time = cadency
        this.remainingShoot = remainingShoot
    }


    override fun tick() {
        super.tick()
        val world=world
        if(!world.isClient){
            world as ServerWorld
            time--
            if(time<0){
                if(remainingShoot<=0) kill()
                time=cadency
                remainingShoot--

                val color=this.spell.color
                val power=this.power
                sendParticleEffect(
                    world,
                    MagicParticleEffect( color, 0.5f ),
                    pos,
                    AdvancedParticleMessage.BOOM,
                    Vec3d(1.0,1.0,1.0),
                    10.0
                )
                spell.use(SpellContext.at(this, power))

                if(remainingShoot<=0) kill()
            }
        }
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        cadency=nbt.getInt("cadency")
        time=nbt.getInt("time")
        remainingShoot=nbt.getInt("remaining_shoot")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt("cadency", cadency)
        nbt.putInt("time", time)
        nbt.putInt("remaining_shoot", remainingShoot)
    }
}