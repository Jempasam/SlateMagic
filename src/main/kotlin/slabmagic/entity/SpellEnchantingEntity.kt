package slabmagic.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import slabmagic.entity.data.SpellEntity
import slabmagic.network.messages.AdvancedParticleMessage
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.MagicParticleEffect
import slabmagic.spell.SpellContext
import slabmagic.spell.build.AssembledSpell

class SpellEnchantingEntity : SpellFollowingEntity{

    private var remainingshoot=1

    constructor(type: EntityType<*>, world: World) : super(type, world)

    constructor(type: EntityType<*>, world: World, spell: AssembledSpell, context: SpellContext.Stored, range: Float, remainingShoot: Int, endSpell: AssembledSpell?=null)
            : super(type, world, SpellEntity.Data(listOfNotNull(spell,endSpell), context), range)
    {
        this.range = range
        this.remainingshoot = remainingShoot
    }

    override fun tickTarget(target: Entity) {
        if(!world.isClient && target is LivingEntity){
            val range=range.toDouble()
            if(target.lastAttackTime==target.age-1)target.age--
            if(target.lastAttackTime==target.age){
                target.age+=2
                val context= SpellContext.at(target,stored)
                val spell=if(spells.size>1 && remainingshoot==1) spells[1].effect else spells[0].effect
                spell.use(context)
                sendParticleEffect(context.world,
                    MagicParticleEffect(spell.color, 0.5f),
                    context.pos,
                    AdvancedParticleMessage.Shape.SHOCKWAVE,
                    Vec3d(range,0.0,range),
                    15.0*range
                )
                remainingshoot--
                if(remainingshoot<=0)kill()
            }
        }
    }

    override fun pos(target: Entity): Vec3d{
        val pos=Vec3d.fromPolar(target.pitch, target.yaw)
        return target.pos.add(pos.x, pos.y+target.standingEyeHeight.toDouble(), pos.z)
    }


    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        remainingshoot=nbt.getInt("remaining_shoot")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt("remaining_shoot", remainingshoot)
    }
}