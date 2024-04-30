package slabmagic.spell.effect.misc

import org.joml.Vector3f
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect

class MarkPosSpellEffect(val spell: SpellEffect): SpellEffect {
    override val color: Vector3f get() = spell.color
    override val cost: Int get() = spell.cost
    override val description get() = spell.description
    override val name get() = spell.name
    override val shape get() = spell.shape

    override fun use(context: SpellContext): SpellContext? {
        context.markeds.add(context.pos)
        return spell.use(context)
    }
}