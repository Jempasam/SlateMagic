package slatemagic.spell.effect.action

import net.minecraft.entity.EntityType
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3f
import slatemagic.helper.ColorTools
import slatemagic.network.messages.sendParticleEffect
import slatemagic.particle.SlateMagicParticles
import slatemagic.particle.SpellCircleParticleEffect
import slatemagic.shape.SpellShape
import slatemagic.spell.effect.SpellEffect
import slatemagic.spell.SpellContext

class EntitySpellEffect(val type: EntityType<*>): SpellEffect {
    override fun use(context: SpellContext): SpellContext? {
        val spawned= type.create(context.world) ?: return null
        context.world.spawnEntity(spawned)
        spawned.setPosition(context.pos)
        sendParticleEffect( context.world,
            SpellCircleParticleEffect(SlateMagicParticles.SPELL_CIRCLE, shape, ColorTools.int(color), 1f, 50),
            context.pos
        )
        return SpellContext.at(spawned, context.power)
    }

    override val name: Text get() = type.name

    override val description: Text get() = Text.of("summon a ").apply { siblings.add(type.name) }

    override val cost: Int get() = 10

    override val color: Vec3f get() = ColorTools.vec(ColorTools.of(type))

    override val shape: SpellShape get() = SpellShape(
        Array(4) {SpellShape.Circle(8, 30, 0, 0, 1, 0, 0)}
        .also {
            it[0]= SpellShape.Circle(16, 0, 0, 0, 1, 0, 0)
            it[1]= SpellShape.Circle(16, 35, 0, 0, 1, 30, 8)
        }
    )

}