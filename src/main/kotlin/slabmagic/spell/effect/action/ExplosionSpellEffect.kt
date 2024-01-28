package slabmagic.spell.effect.action

import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import net.minecraft.world.explosion.Explosion
import slabmagic.helper.ColorTools
import slabmagic.network.messages.AdvancedParticleMessage
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.MagicParticleEffect
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect
import slabmagic.spell.spellDesc
import slabmagic.spell.spellName

class ExplosionSpellEffect(val power: Float=1f, val createFire: Boolean=false): SpellEffect {
    override fun use(context: SpellContext): SpellContext {
        context.world.createExplosion(null, context.pos.x, context.pos.y, context.pos.z, 1.0f+power*context.power.toFloat()/2, createFire, Explosion.DestructionType.DESTROY)
        val ppower=context.power/10.0
        sendParticleEffect(
            context.world,
            MagicParticleEffect(color, 0.5f),
            context.pos,
            AdvancedParticleMessage.BOOM,
            Vec3d(ppower, ppower, ppower),
            context.power*10.0*power
        )
        return context
    }

    override val name: Text get() = spellName( if(createFire) "fire_explosion" else "explosion" )

    override val description: Text get() = spellDesc( if(createFire) "fire_explosion" else "explosion", power)

    override val cost: Int get() = (20*power).toInt()

    override val color: Vec3f get() = ColorTools.vec(if(createFire) DyeColor.ORANGE.signColor else DyeColor.RED.signColor)

    override val shape: SpellShape get() = SpellShape(Array(4){SpellShape.Circle(16, 20, 0, 0, 1, 40, 8)})

}