package slabmagic.spell.effect.misc

import net.minecraft.text.Text
import org.joml.Vector3f
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect
import kotlin.math.ceil

class ProbabilitySpellEffect(val a: SpellEffect, val probability: Float) : SpellEffect{
    override val description: Text get() = Text.literal((probability*100).toInt().toString()+"% chance that ").apply {
        append(a.description)
    }

    override val name: Text get() = Text.literal("Random ").apply {
        append(a.name)
    }

    override val cost: Int get() = ceil(a.cost*probability).toInt()

    override val color: Vector3f = Vector3f(1f,1f,1f).lerp(a.color,probability)

    override val shape: SpellShape get() = a.shape

    override fun use(context: SpellContext): SpellContext? {
        return if(Math.random()<=probability) a.use(context) else null
    }

}