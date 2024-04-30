package slabmagic.spell.effect.misc

import net.minecraft.text.Text
import org.joml.Vector3f
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect

class ChoiceSpellEffect(val a: SpellEffect, val b: SpellEffect) : SpellEffect{
    override val description: Text get() = a.description.copy().append(Text.of(" or ")).append(b.description)

    override val name: Text get() = a.name.copy().apply {
        append(Text.of(" or "))
        append(b.name)
    }

    override val cost: Int get() = (a.cost+b.cost)/2

    override val color: Vector3f = Vector3f(a.color).lerp(b.color,0.5f)

    override val shape: SpellShape get() = a.shape.apply {
        val bshape=b.shape
        this[2] = bshape[0]
        this[3] = bshape[1]
        this[1].apply {
            subCircleCount=(subCircleRadius+1).toByte()
            if(subCircleRadius<10)subCircleRadius=10
            spacing=(spacing+10).toByte()
        }
    }

    override fun use(context: SpellContext): SpellContext? {
        return if(Math.random()>0.5) a.use(context) else b.use(context)
    }


}