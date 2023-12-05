package slabmagic.spell.effect.move

import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import slabmagic.entity.SlabMagicEntities
import slabmagic.entity.SpellShieldEntity
import slabmagic.network.messages.AdvancedParticleMessage
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.MagicParticleEffect
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.build.parts.AssembledSpell
import slabmagic.spell.effect.SpellEffect
import kotlin.math.cbrt
import kotlin.math.sqrt

class ShieldSpellEffect(val range: Float, val count: Int, val decorated: AssembledSpell): SpellEffect {

    override fun use(context: SpellContext): SpellContext? {
        val levelRange=range* cbrt(context.power.toFloat())
        val levelCount= count* sqrt(context.power.toDouble()).toInt()
        val trap=SpellShieldEntity(SlabMagicEntities.SPELL_SHIELD, context.world, decorated, context.power, levelRange, levelCount)
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

    override val name: Text get() = Text.of("Shield of ").also { it.siblings.add(decorated.effect.name) }

    override val description: Text get() = Text.of("summon a shield targeting an entity $range that on hit, $count times, ").also { it.siblings.add(decorated.effect.description) }

    override val cost: Int get() = (decorated.effect.cost*(1.0+range/2.0)*count).toInt()

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