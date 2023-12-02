package slabmagic.spell.effect.move

import net.minecraft.text.Text
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect

class AtEyeSpellEffect(val effect: SpellEffect): SpellEffect by effect {
    override val description: Text get() = Text.of("at eye height, ").also { it.siblings.add(effect.description) }
    override val name: Text get() = Text.of("Eye-").also { it.siblings.add(effect.name) }

    override fun use(context: SpellContext): SpellContext? {
        context.entity?.let { context.pos=it.eyePos }
        return effect.use(context)
    }
}