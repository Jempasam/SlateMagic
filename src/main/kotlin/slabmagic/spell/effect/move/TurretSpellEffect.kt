package slabmagic.spell.effect.move

import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f
import slabmagic.entity.SlabMagicEntities
import slabmagic.entity.SpellTurretEntity
import slabmagic.network.messages.AdvancedParticleMessage
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.MagicParticleEffect
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.build.AssembledSpell
import slabmagic.spell.effect.SpellEffect
import slabmagic.spell.times
import slabmagic.utils.coerceIn
import kotlin.math.sqrt

class TurretSpellEffect(val cadency: Int, val count: Int, val decorated: AssembledSpell, val endSpell: AssembledSpell?=null): SpellEffect {

    override fun use(context: SpellContext): SpellContext {
        val levelCount= (count*sqrt(context.stored.power.toDouble())).toInt()
        val trap=SpellTurretEntity(SlabMagicEntities.SPELL_TURRET, context.world, decorated, context.stored, cadency, levelCount, endSpell)
        trap.setPosition(context.pos)
        trap.pitch=context.direction.x
        trap.yaw=context.direction.y
        context.world.spawnEntity(trap)
        sendParticleEffect(context.world,
            MagicParticleEffect(color, 0.5f),
            context.pos,
            AdvancedParticleMessage.Shape.SHOCKWAVE,
            Vec3d(1.0,0.0,1.0),
            20.0
        )
        return SpellContext.at(trap,context.stored)
    }

    override val name: Text get() {
        return if(count==1) Text.literal("Delayed ").append(decorated.effect.name)
        else Text.literal("Turret of ").append(decorated.effect.name)
    }

    override val description: Text get() =
        if(count==1) Text.literal("after some time ").append((endSpell?:decorated).effect.description)
        else if(endSpell==null) Text.literal("summon a turret that").times(count).append(decorated.effect.description)
        else Text.literal("summon a turret that").times(count-1).append(decorated.effect.description).append(" then ").append(endSpell.effect.description)

    override val cost: Int get() = decorated.effect.cost*count

    override val color: Vector3f get() = decorated.effect.color.apply {
        add(-0.1f,0.1f,-0.1f)
        coerceIn(0.0f,1.0f)
    }

    override val shape: SpellShape get() = decorated.effect.shape.also {
        it[2].subCircleCount = (it[2].subCircleCount + count).toByte()
        it[2].subCircleRadius = (it[2].subCircleRadius + 5+count*5).toByte()
    }

}