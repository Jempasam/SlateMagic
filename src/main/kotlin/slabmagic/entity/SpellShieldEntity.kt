package slabmagic.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import slabmagic.helper.asAngle
import slabmagic.helper.minus
import slabmagic.network.messages.AdvancedParticleMessage
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.MagicParticleEffect
import slabmagic.spell.SpellContext
import slabmagic.spell.build.AssembledSpell

class SpellShieldEntity : SpellFollowingEntity{

    private var remainingshoot=1

    constructor(type: EntityType<*>, world: World) : super(type, world)

    constructor(type: EntityType<*>, world: World, spell: AssembledSpell, power: Int, range: Float, remainingShoot: Int, endSpell: AssembledSpell?=null)
            : super(type, world, listOfNotNull(spell,endSpell), power, range)
    {
        this.range = range
        this.remainingshoot = remainingShoot
    }

    override fun tickTarget(target: Entity) {

        if(!world.isClient && target is LivingEntity){
            val range=range.toDouble()
            val attacker=target.attacker
            if(target.lastAttackedTime==target.age-1)target.age--
            if(target.lastAttackedTime==target.age && attacker!=null){
                target.age+=2

                // Pos and direction
                val mih= range/2.0
                val direction= (attacker.eyePos-target.eyePos).asAngle()

                // Context
                val context= SpellContext.atEye(target,power)
                context.direction=direction
                context.markeds.addAll(markeds)

                val spell=if(spells.size>1 && remainingshoot==1) spells[1].effect else spells[0].effect
                spell.use(context)
                sendParticleEffect(context.world,
                    MagicParticleEffect(spell.color, 0.5f),
                    context.pos,
                    AdvancedParticleMessage.SHOCKWAVE,
                    Vec3d(range,0.0,range),
                    15.0*range
                )
                remainingshoot--
                if(remainingshoot<=0)kill()
            }
        }
    }

    override fun pos(target: Entity) = target.pos.add(0.0,target.height/2.0,0.0)

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        remainingshoot=nbt.getInt("remaining_shoot")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt("remaining_shoot", remainingshoot)
    }
}