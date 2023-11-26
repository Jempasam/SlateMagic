package slatemagic.spell.effect.move

import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import slatemagic.entity.SlateMagicEntities
import slatemagic.entity.SpellTrapEntity
import slatemagic.network.messages.AdvancedParticleMessage
import slatemagic.network.messages.sendParticleEffect
import slatemagic.particle.MagicParticleEffect
import slatemagic.shape.SpellShape
import slatemagic.spell.SpellContext
import slatemagic.spell.build.AssembledSpell
import slatemagic.spell.effect.SpellEffect
import kotlin.math.cbrt
import kotlin.math.sqrt

class TrapSpellEffect(val range: Float, val count: Int, val decorated: AssembledSpell): SpellEffect {

    override fun use(context: SpellContext): SpellContext? {
        val levelRange=range* cbrt(context.power.toFloat())
        val levelCount= count* sqrt(context.power.toDouble()).toInt()
        val trap=SpellTrapEntity(SlateMagicEntities.SPELL_TRAP, context.world, decorated, context.power, levelRange, levelCount)
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

    override val name: Text get() = Text.of("Trap of ").also { it.siblings.add(decorated.effect.name) }

    override val description: Text get() = Text.of("summon a trap with a range of $range that ").also { it.siblings.add(decorated.effect.description) }

    override val cost: Int get() = (decorated.effect.cost*(1.0+range/2.0)*count).toInt()

    override val color: Vec3f get() = decorated.effect.color.apply {
        add(-0.05f,0.0f,-0.1f)
        clamp(0.0f,1.0f)
    }

    override val shape: SpellShape get() = decorated.effect.shape.also {
        if(it[3].cornerCount>4)it[3].cornerCount--
        else if(it[3].cornerCount<4)it[3].cornerCount++
        it[3].repetition++
    }

}