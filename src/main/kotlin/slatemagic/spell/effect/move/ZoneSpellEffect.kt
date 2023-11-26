package slatemagic.spell.effect.move

import net.minecraft.text.Text
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import slatemagic.network.messages.AdvancedParticleMessage
import slatemagic.network.messages.sendParticleEffect
import slatemagic.particle.MagicParticleEffect
import slatemagic.shape.SpellShape
import slatemagic.spell.SpellContext
import slatemagic.spell.effect.SpellEffect
import kotlin.math.cbrt
import kotlin.math.max

class ZoneSpellEffect(val zone: Vec3d, val decorated: SpellEffect): SpellEffect {

    override fun use(context: SpellContext): SpellContext? {
        val ppower= cbrt(context.power.toDouble())
        val leveledZone=zone.multiply(ppower)
        val targets=context.world.getOtherEntities(null, Box.of(context.pos,leveledZone.x,leveledZone.y,leveledZone.z))
        var lastContext: SpellContext?=null
        for(target in targets){
            val subContext= SpellContext.at(target, context.power)
            subContext.markeds.addAll(context.markeds)
            subContext.direction=context.direction
            lastContext=decorated.use(subContext) ?: lastContext
        }
        sendParticleEffect(
            context.world,
            MagicParticleEffect(color, 0.3f),
            context.pos,
            AdvancedParticleMessage.CLOUD,
            leveledZone.multiply(0.5),
            max(4.0,leveledZone.x*leveledZone.y*leveledZone.z/4.0)
        )
        return lastContext
    }

    override val name: Text get() = Text.of("Zone ").also { it.siblings.add(decorated.name) }

    override val description: Text get() = Text.of("in a zone of dimension $zone, ").also { it.siblings.add(decorated.description) }

    override val cost: Int get() = (decorated.cost*(1.0+zone.x*zone.y*zone.z/150.0)).toInt()

    override val color: Vec3f get() = decorated.color.apply {
        add(0.1f,-0.1f,0.1f)
        clamp(0.0f,1.0f)
    }

    override val shape: SpellShape get() = decorated.shape.also {
        it[0].spacing=(it[0].spacing+4+cbrt(zone.x*zone.y*zone.z*3).toInt()).toByte()
        it[2].cornerCount=(it[2].cornerCount+4+cbrt(zone.x*zone.y*zone.z).toInt()).toByte()
    }

}