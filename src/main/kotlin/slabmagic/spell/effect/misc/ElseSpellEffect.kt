package slabmagic.spell.effect.misc

import net.minecraft.text.Text
import net.minecraft.util.math.Vec3f
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect

class ElseSpellEffect(val a: SpellEffect, val b: SpellEffect) : SpellEffect{
    override val description: Text get() = a.description.copy().append(Text.of(" else ")).append(b.description)

    override val name: Text get() = a.name.copy().apply {
        append(Text.of(" else "))
        append(b.name)
    }

    override val cost: Int get() = a.cost+b.cost

    override val color: Vec3f = a.color.copy().apply { lerp(b.color,0.4f) }

    override val shape: SpellShape get() = a.shape.apply {
        val bshape=b.shape
        this[3] = bshape[0]
        this[4].spacing=(this[4].spacing-10).toByte()
    }

    override fun use(context: SpellContext): SpellContext? {
        return a.use(context.copy()) ?: b.use(context)
    }


}