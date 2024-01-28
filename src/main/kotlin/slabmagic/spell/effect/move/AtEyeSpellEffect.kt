package slabmagic.spell.effect.move

import net.minecraft.text.Text
import net.minecraft.util.math.Vec2f
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect
import slabmagic.spell.spellDesc
import slabmagic.spell.spellName

class AtEyeSpellEffect(val effect: SpellEffect): SpellEffect by effect {

    override val name: Text get() = spellName("at_eye", effect.name)
    override val description get() = spellDesc("at_eye", effect.description)

    override fun use(context: SpellContext): SpellContext? {
        context.entity?.let {
            context.pos=it.eyePos
            context.direction= Vec2f(it.pitch, it.yaw)
        }
        return effect.use(context)
    }
}