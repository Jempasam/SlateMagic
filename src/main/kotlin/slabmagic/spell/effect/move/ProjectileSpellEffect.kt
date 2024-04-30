package slabmagic.spell.effect.move

import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f
import slabmagic.entity.SlabMagicEntities
import slabmagic.entity.SpellProjectileEntity
import slabmagic.network.messages.AdvancedParticleMessage
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.MagicParticleEffect
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.build.AssembledSpell
import slabmagic.spell.effect.SpellEffect
import slabmagic.utils.coerceIn
import kotlin.math.max
import kotlin.math.sqrt

class ProjectileSpellEffect(val strength: Float, val duration: Int, val decorated: AssembledSpell, val divergence: Float=0f):
    SpellEffect {

    override fun use(context: SpellContext): SpellContext {
        val levelDuration= (duration*sqrt(context.stored.power.toDouble())).toInt()
        val levelStrength= (strength*sqrt(context.stored.power.toFloat()))

        val proj=SpellProjectileEntity(SlabMagicEntities.SPELL_PROJECTILE, context.world, decorated, context.stored, levelDuration)
        proj.setVelocity(context.direction.x, context.direction.y, levelStrength, divergence)
        proj.setPosition(context.pos)
        context.world.spawnEntity(proj)
        sendParticleEffect(context.world,
            MagicParticleEffect(color, 0.5f),
            context.pos,
            AdvancedParticleMessage.Shape.BOOM,
            Vec3d(0.2,0.2,0.2),
            5.0,
            proj.velocity
        )
        return SpellContext.at(proj,context.stored)
    }

    override val name: Text get() {
        return Text.literal("Gun of ").append(decorated.effect.name)
    }

    override val description: Text get(){
        return Text.literal("shoot a projectile that ").append(decorated.effect.description)
    }

    override val cost: Int get() = (decorated.effect.cost*(1f+duration/40f+strength/1f)).toInt()

    override val color: Vector3f get() = decorated.effect.color.apply {
        add(0.1f,0.1f,0.1f)
        coerceIn(0.0f,1.0f)
    }

    override val shape: SpellShape get() = decorated.effect.shape.also {
        it[2].apply {
            this.cornerCount= 6
            this.succionDepth= (this.succionDepth + 80*max(1f,strength/1f+duration/400f)).toInt().toByte()
        }
    }

}