package slatemagic.spell.effect.misc

import net.minecraft.util.math.Vec3f
import slatemagic.spell.SpellContext
import slatemagic.spell.effect.SpellEffect

class MarkPosSpellEffect(val spell: SpellEffect): SpellEffect {
    override val color: Vec3f get() = spell.color
    override val cost: Int get() = spell.cost
    override val description get() = spell.description
    override val name get() = spell.name
    override val shape get() = spell.shape

    override fun use(context: SpellContext): SpellContext? {
        context.markeds.add(context.pos)
        return spell.use(context)
    }
}