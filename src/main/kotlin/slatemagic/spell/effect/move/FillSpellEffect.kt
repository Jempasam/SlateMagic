package slatemagic.spell.effect.move

import net.minecraft.text.Text
import net.minecraft.util.math.Vec3f
import net.minecraft.util.math.Vec3i
import slatemagic.shape.SpellShape
import slatemagic.spell.SpellContext
import slatemagic.spell.effect.SpellEffect
import kotlin.math.sqrt

class FillSpellEffect(val spell: SpellEffect, val size: Vec3i): SpellEffect {
    override val description: Text get() = Text.of("fill an aera of ${size.x}x${size.y}x${size.z} with ").apply { siblings.add(spell.name) }

    override val name: Text get() = Text.of("Square of ").apply { siblings.add(spell.name) }

    override val color: Vec3f get() = spell.color

    override val cost: Int get() = (sqrt(size.x*size.y*size.z.toDouble())*spell.cost).toInt()

    override val shape: SpellShape get() = spell.shape.apply { this[0].cornerCount=4 }

    override fun use(context: SpellContext): SpellContext? {
        var ret: SpellContext? = null
        val corner=context.pos.subtract(size.x.toDouble()/2, size.y.toDouble()/2, size.z.toDouble()/2)
        for (i in 0 until size.x)
            for (j in 0 until size.y)
                for (k in 0 until size.z) {
                    val subctx = context.copy()
                    subctx.pos=corner.add(i.toDouble(), j.toDouble(), k.toDouble())
                    val ctx = spell.use(subctx)
                    if (ctx == null) ret=ctx
                }
        return ret
    }
}