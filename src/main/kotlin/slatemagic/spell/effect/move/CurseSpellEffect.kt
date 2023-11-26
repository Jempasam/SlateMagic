package slatemagic.spell.effect.move

import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import slatemagic.entity.SlateMagicEntities
import slatemagic.entity.SpellCurseEntity
import slatemagic.network.messages.AdvancedParticleMessage
import slatemagic.network.messages.sendParticleEffect
import slatemagic.particle.MagicParticleEffect
import slatemagic.shape.SpellShape
import slatemagic.spell.SpellContext
import slatemagic.spell.build.AssembledSpell
import slatemagic.spell.effect.SpellEffect
import kotlin.math.cbrt
import kotlin.math.max
import kotlin.math.sqrt

class CurseSpellEffect(val range: Float, val count: Int, val cadency: Int, val decorated: AssembledSpell): SpellEffect {

    override fun use(context: SpellContext): SpellContext? {
        val levelRange=range* cbrt(context.power.toFloat())
        val levelCount= count* sqrt(context.power.toDouble()).toInt()
        val levelCadency= max(1, (cadency*(1.0f-sqrt(context.power.toDouble())/10f)).toInt())
        val trap=SpellCurseEntity(SlateMagicEntities.SPELL_CURSE, context.world, decorated, context.power, levelRange, levelCount, levelCadency)
        trap.setPosition(context.pos)
        trap.pitch=context.direction.x
        trap.yaw=context.direction.y
        context.world.spawnEntity(trap)
        sendParticleEffect(context.world,
            MagicParticleEffect(color, 0.5f),
            context.pos,
            AdvancedParticleMessage.SHOCKWAVE,
            Vec3d(1.0,0.0,1.0),
            15.0*range
        )
        return SpellContext.at(trap,context.power)
    }

    override val name: Text get() = Text.of("Curse of ").also { it.siblings.add(decorated.effect.name) }

    override val description: Text get() = Text.of("summon a curse targeting an entity in a range of $range that, $count times, ").also { it.siblings.add(decorated.effect.description) }

    override val cost: Int get() = (decorated.effect.cost*(1.0+range/2.0)*count*(1.0f-sqrt(cadency.toDouble())/50)).toInt()

    override val color: Vec3f get() = decorated.effect.color.apply {
        add(-0.1f,0.0f,-0.05f)
        clamp(0.0f,1.0f)
    }

    override val shape: SpellShape get() = decorated.effect.shape.also {
        it[2].apply {
            repetition++
            if(cornerCount>5)cornerCount--
            if(spacing>10)spacing--
        }
    }

}