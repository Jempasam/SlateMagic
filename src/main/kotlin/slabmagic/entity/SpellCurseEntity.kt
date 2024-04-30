package slabmagic.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import slabmagic.entity.data.SpellEntity
import slabmagic.network.messages.AdvancedParticleMessage
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.MagicParticleEffect
import slabmagic.spell.SpellContext
import slabmagic.spell.build.AssembledSpell

class SpellCurseEntity : SpellFollowingEntity{

    private var remainingshoot=1
    private var time=0
    private var cadency=0

    constructor(type: EntityType<*>, world: World) : super(type, world)

    constructor(type: EntityType<*>, world: World, spell: AssembledSpell, context: SpellContext.Stored, range: Float, remainingShoot: Int, cadency: Int, endSpell: AssembledSpell?=null)
            : super(type, world, SpellEntity.Data(listOfNotNull(spell,endSpell),context), range)
    {
        this.range = range
        this.remainingshoot = remainingShoot
        this.cadency = cadency
    }

    override fun tickTarget(target: Entity) {
        if(world.isClient)return
        if(remainingshoot<=0)kill()
        time++
        val range=range.toDouble()
        if(time>cadency){
            val context= SpellContext.at(target,stored)
            val spell= if(spells.size>1 && remainingshoot==1) spells[1].effect else spells[0].effect
            spell.use(context)
            sendParticleEffect(context.world,
                MagicParticleEffect(spell.color, 0.5f),
                context.pos,
                AdvancedParticleMessage.Shape.SHOCKWAVE,
                Vec3d(range,0.0,range),
                15.0*range
            )
            time=0
            remainingshoot--
            if(remainingshoot<=0)kill()
        }
    }

    override fun pos(target: Entity) = target.pos.add(0.0,0.1,0.0)

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        time=nbt.getInt("time")
        remainingshoot=nbt.getInt("remaining_shoot")
        cadency=nbt.getInt("cadency")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt("time", time)
        nbt.putInt("remaining_shoot", remainingshoot)
        nbt.putInt("cadency", cadency)
    }
}