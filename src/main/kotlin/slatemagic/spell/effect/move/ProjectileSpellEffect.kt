package slatemagic.spell.effect.move

import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import slatemagic.entity.SlateMagicEntities
import slatemagic.entity.SpellProjectileEntity
import slatemagic.network.messages.AdvancedParticleMessage
import slatemagic.network.messages.sendParticleEffect
import slatemagic.particle.MagicParticleEffect
import slatemagic.shape.SpellShape
import slatemagic.spell.SpellContext
import slatemagic.spell.build.SPELL
import slatemagic.spell.build.SpellNode
import slatemagic.spell.build.SpellPart
import slatemagic.spell.effect.SpellEffect
import kotlin.math.max
import kotlin.math.sqrt

class ProjectileSpellEffect(val strength: Float, val duration: Int, val decorated: SpellEffect, val divergence: Float=0f):
    SpellEffect {

    override fun use(context: SpellContext): SpellContext? {
        val levelDuration= (duration*sqrt(context.power.toDouble())).toInt()
        val levelStrength= (strength*sqrt(context.power.toFloat()))

        val proj=SpellProjectileEntity(SlateMagicEntities.SPELL_PROJECTILE, context.world, decorated, context.power, levelDuration)
        proj.setVelocity(context.direction.x, context.direction.y, levelStrength, divergence)
        proj.setPosition(context.pos)
        context.world.spawnEntity(proj)
        sendParticleEffect(context.world,
            MagicParticleEffect(color, 0.5f),
            context.pos,
            AdvancedParticleMessage.BOOM,
            Vec3d(0.2,0.2,0.2),
            5.0,
            proj.velocity
        )
        return SpellContext.at(proj,context.power)
    }

    override val name: Text get() {
        return Text.of("Gun of ").also { it.siblings.add(decorated.name) }
    }

    override val description: Text get(){
        return Text.of("shoot a projectile that ").also { it.siblings.add(decorated.description) }
    }

    override val cost: Int get() = (decorated.cost*(1f+duration/40f+strength/1f)).toInt()

    override val color: Vec3f get() = decorated.color.apply {
        add(0.1f,0.1f,0.1f)
        clamp(0.0f,1.0f)
    }

    override val shape: SpellShape get() = decorated.shape.also {
        it[2].apply {
            this.cornerCount= 6
            this.succionDepth= (this.succionDepth + 80*max(1f,strength/1f+duration/400f)).toInt().toByte()
        }
    }

    object Node: SpellNode<SpellEffect> {
        override val parameters = listOf(SPELL)
        override val name = "Projectile"
        override fun build(parts: List<SpellPart<*>>): SpellPart<SpellEffect> {
            return SPELL.create(ProjectileSpellEffect(0.5f,100,SPELL.cast(parts[0])?:throw Exception()))
        }
    }

}