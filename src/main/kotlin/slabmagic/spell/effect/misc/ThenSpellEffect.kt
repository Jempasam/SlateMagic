package slabmagic.spell.effect.misc

import net.minecraft.text.Text
import org.joml.Vector3f
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect

class ThenSpellEffect(val a: SpellEffect, val b: SpellEffect) : SpellEffect{
    override val description: Text get() = a.description.copy().append(Text.of(" then ")).append(b.description)

    override val name: Text get() = a.name.copy().apply {
        append(Text.of(" then "))
        append(b.name)
    }

    override val cost: Int get() = a.cost+b.cost

    override val color: Vector3f = Vector3f(a.color).apply { lerp(b.color,0.4f) }

    override val shape: SpellShape get() = a.shape.apply {
        val bshape=b.shape
        this[3] = bshape[0]
        this[4].spacing=(this[4].spacing-10).toByte()
    }

    override fun use(context: SpellContext): SpellContext? {
        val acontext= a.use(context) ?: return null
        return b.use(acontext)
    }


}