package slabmagic.spell.effect

import org.joml.Vector3f
import slabmagic.spell.SpellContext

class PrecalculatedSpellEffect(val spell: SpellEffect): SpellEffect {

    override fun use(context: SpellContext): SpellContext? = spell.use(context)

    override val name = spell.name

    override val description = spell.description

    override val cost = spell.cost

    override val color: Vector3f = spell.color

    override val shape = spell.shape

}