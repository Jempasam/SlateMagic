package slatemagic.spell.action

import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import net.minecraft.world.explosion.Explosion
import slatemagic.helper.ColorTools
import slatemagic.network.messages.AdvancedParticleMessage
import slatemagic.network.messages.sendParticleEffect
import slatemagic.particle.MagicParticleEffect
import slatemagic.shape.SpellShape
import slatemagic.spell.Spell
import slatemagic.spell.SpellContext

class ExplosionSpell: Spell {
    override fun use(context: SpellContext): SpellContext {
        context.world.createExplosion(null, context.pos.x, context.pos.y, context.pos.z, 0.5f+context.power.toFloat()/2, false, Explosion.DestructionType.DESTROY)
        val ppower=context.power/10.0
        sendParticleEffect(
            context.world,
            MagicParticleEffect(color, 0.5f),
            context.pos,
            AdvancedParticleMessage.BOOM,
            Vec3d(ppower, ppower, ppower),
            context.power*10.0
        )
        return context
    }

    override val name: Text get() = Text.of("Explosion")

    override val description: Text get() = Text.of("create an explosion")

    override val cost: Int get() = 20

    override val color: Vec3f get() = ColorTools.vec(DyeColor.ORANGE.signColor)

    override val shape: SpellShape get() = SpellShape(Array(4){SpellShape.Circle(16, 20, 0, 0, 1, 40, 8)})

}