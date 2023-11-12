package slatemagic.spell

import net.minecraft.util.math.Vec3f

class PrecalculatedSpell(val spell: Spell): Spell{

    override fun use(context: SpellContext): SpellContext? = spell.use(context)

    override val name = spell.name

    override val description = spell.description

    override val cost = spell.cost

    override val color: Vec3f = spell.color

    override val shape = spell.shape

}