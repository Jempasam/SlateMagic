package slabmagic.spell.effect.move

import net.minecraft.text.Text
import net.minecraft.util.math.Vec3f
import net.minecraft.util.math.Vec3i
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect
import slabmagic.spell.spellDesc
import slabmagic.spell.spellName
import kotlin.math.sqrt

class FillSpellEffect(val spell: SpellEffect, val size: Vec3i): SpellEffect {

    override val name: Text get() = spellName("fill", spell.name)
    override val description: Text get() = spellDesc("fill", size.x, size.y, size.z, spell.description)

    override val color: Vec3f get() = spell.color

    override val cost: Int get() = (sqrt(size.x*size.y*size.z.toDouble())*spell.cost).toInt()

    override val shape: SpellShape get() = spell.shape.apply { this[0].cornerCount=4 }

    override fun use(context: SpellContext): SpellContext? {
        var ret: SpellContext? = null
        val corner=context.pos.add(
            if(size.x>1) -size.x.toDouble()/2+.5 else 0.0,
            if(size.y>1) -size.y.toDouble()/2 else 0.0,
            if(size.z>1) -size.z.toDouble()/2+.5 else 0.0
        )
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