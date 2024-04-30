package slabmagic.spell.effect.move

import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f
import slabmagic.entity.SlabMagicEntities
import slabmagic.entity.SpellCurseEntity
import slabmagic.network.messages.AdvancedParticleMessage
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.MagicParticleEffect
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.build.AssembledSpell
import slabmagic.spell.effect.SpellEffect
import slabmagic.spell.times
import slabmagic.utils.coerceIn
import kotlin.math.cbrt
import kotlin.math.max
import kotlin.math.sqrt

class CurseSpellEffect(val range: Float, val count: Int, val cadency: Int, val decorated: AssembledSpell, val endSpell: AssembledSpell?=null): SpellEffect {

    override fun use(context: SpellContext): SpellContext {
        val levelRange=range* cbrt(context.stored.power.toFloat())
        val levelCount= count* sqrt(context.stored.power.toDouble()).toInt()
        val levelCadency= max(1, (cadency*(1.0f-sqrt(context.stored.power.toDouble())/10f)).toInt())
        val trap=SpellCurseEntity(SlabMagicEntities.SPELL_CURSE, context.world, decorated, context.stored, levelRange, levelCount, levelCadency, endSpell)
        trap.setPosition(context.pos)
        trap.pitch=context.direction.x
        trap.yaw=context.direction.y
        context.world.spawnEntity(trap)
        sendParticleEffect(context.world,
            MagicParticleEffect(color, 0.5f),
            context.pos,
            AdvancedParticleMessage.Shape.SHOCKWAVE,
            Vec3d(1.0,0.0,1.0),
            15.0*range
        )
        return SpellContext.at(trap,context.stored)
    }

    override val name: Text get() = Text.literal("Curse of ").append(decorated.effect.name)

    override val description: Text get() =
        if(count==1) Text.literal("cast a curse on an entity that after ${cadency/20f}s, ").append(decorated.effect.description)
        else if(endSpell==null) Text.literal("cast a curse on an entity that").times(count).append("each ${cadency/20f}s, ").append(decorated.effect.description)
        else if(count==2) Text.literal("cast a curse on an entity that").append(decorated.effect.description).append(", then after ${cadency/20f}s, ").append(endSpell.effect.description)
        else Text.literal("cast a curse on an entity that").times(count-1).append("each ${cadency/20f}s, ").append(decorated.effect.description).append(", then after ${cadency/20f}s, ").append(endSpell.effect.description)

    override val cost: Int get() = (decorated.effect.cost*(1.0+range/2.0)*count*(1.0f-sqrt(cadency.toDouble())/50)).toInt()

    override val color: Vector3f get() = decorated.effect.color.apply {
        add(-0.1f,0.0f,-0.05f)
        coerceIn(0.0f,1.0f)
    }

    override val shape: SpellShape get() = decorated.effect.shape.also {
        it[2].apply {
            repetition++
            if(cornerCount>5)cornerCount--
            if(spacing>10)spacing--
        }
    }

}