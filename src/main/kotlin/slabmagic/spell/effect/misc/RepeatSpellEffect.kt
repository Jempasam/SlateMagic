package slabmagic.spell.effect.misc

import net.minecraft.text.Text
import net.minecraft.util.math.Vec3f
import slabmagic.helper.ColorTools
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.SlabMagicParticles
import slabmagic.particle.SpellCircleParticleEffect
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect

class RepeatSpellEffect(val decorated: SpellEffect, val count: Int) : SpellEffect{
    override val description: Text get() = Text.of("${count} times, ").also { it.siblings.add(decorated.description) }

    override val name: Text get() = Text.of("Multi ").also { it.siblings.add(decorated.name) }

    override val cost: Int get() = decorated.cost*count

    override val color: Vec3f = decorated.color

    override val shape: SpellShape get() = decorated.shape.apply {
        this[0].subCircleCount= (this[0].subCircleCount+1).toByte()
        if(this[0].subCircleRadius<10)this[0].subCircleCount=10
    }

    override fun use(context: SpellContext): SpellContext? {
        var ctx: SpellContext?=null
        for(i in 0 until count){
            decorated.use(context.copy())?.let { ctx=it }
        }
        sendParticleEffect(
            context.world,
            SpellCircleParticleEffect(SlabMagicParticles.SPELL_CROSSED, shape, ColorTools.int(color), 0.5f, 30),
            context.pos
        )
        return ctx
    }


}