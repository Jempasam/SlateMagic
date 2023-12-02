package slabmagic.spell.effect.move

import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import slabmagic.entity.SlabMagicEntities
import slabmagic.entity.SpellTurretEntity
import slabmagic.network.messages.AdvancedParticleMessage
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.MagicParticleEffect
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.build.parts.AssembledSpell
import slabmagic.spell.effect.SpellEffect
import kotlin.math.sqrt

class TurretSpellEffect(val cadency: Int, val count: Int, val decorated: AssembledSpell): SpellEffect {

    override fun use(context: SpellContext): SpellContext? {
        val levelCount= (count*sqrt(context.power.toDouble())).toInt()
        val trap=SpellTurretEntity(SlabMagicEntities.SPELL_TURRET, context.world, decorated, context.power, cadency, levelCount)
        trap.setPosition(context.pos)
        trap.pitch=context.direction.x
        trap.yaw=context.direction.y
        context.world.spawnEntity(trap)
        sendParticleEffect(context.world,
            MagicParticleEffect(color, 0.5f),
            context.pos,
            AdvancedParticleMessage.SHOCKWAVE,
            Vec3d(1.0,0.0,1.0),
            20.0
        )
        return SpellContext.at(trap,context.power)
    }

    override val name: Text get() {
        if(count==1)return Text.of("Delayed ").also { it.siblings.add(decorated.effect.name) }
        else return Text.of("Turret of ").also { it.siblings.add(decorated.effect.name) }
    }

    override val description: Text get(){
        if(count==1)return Text.of("after some time ").also { it.siblings.add(decorated.effect.description) }
        else return Text.of("summon a turret that, $count times, ").also { it.siblings.add(decorated.effect.description) }
    }

    override val cost: Int get() = decorated.effect.cost*count

    override val color: Vec3f get() = decorated.effect.color.apply {
        add(-0.1f,0.1f,-0.1f)
        clamp(0.0f,1.0f)
    }

    override val shape: SpellShape get() = decorated.effect.shape.also {
        it[2].subCircleCount = (it[2].subCircleCount + count).toByte()
        it[2].subCircleRadius = (it[2].subCircleRadius + 5+count*5).toByte()
    }

}