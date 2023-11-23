package slatemagic.spell.effect.misc

import net.minecraft.text.Text
import net.minecraft.util.math.Vec3f
import slatemagic.shape.SpellShape
import slatemagic.spell.SpellContext
import slatemagic.spell.effect.SpellEffect

class RepeatSpellEffect(val decorated: SpellEffect, val count: Int) : SpellEffect{
    override val description: Text get() = Text.of("${count} times, ").also { it.siblings.add(decorated.description) }

    override val name: Text get() = Text.of("Multi ").also { it.siblings.add(decorated.name) }

    override val cost: Int get() = decorated.cost*count

    override val color: Vec3f = decorated.color

    override val shape: SpellShape get() = decorated.shape.apply {
        this[0].subCircleCount= (this[0].subCircleCount+1).toByte()
    }

    override fun use(context: SpellContext): SpellContext? {
        var ctx: SpellContext?=null
        for(i in 0 until count){
            decorated.use(context)?.let { ctx=it }
        }
        return ctx
    }


}