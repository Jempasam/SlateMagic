package slatemagic.spell.move

import net.minecraft.text.Text
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import slatemagic.network.messages.AdvancedParticleMessage
import slatemagic.network.messages.sendParticleEffect
import slatemagic.particle.MagicParticleEffect
import slatemagic.shape.SpellShape
import slatemagic.spell.Spell
import slatemagic.spell.SpellContext
import kotlin.math.cbrt

class ZoneSpell(val zone: Vec3d, val decorated: Spell): Spell {

    override fun use(context: SpellContext): SpellContext? {
        val ppower= cbrt(context.power.toDouble())
        val leveled_zone=zone.multiply(ppower)
        val targets=context.world.getOtherEntities(null, Box.of(context.pos,leveled_zone.x,leveled_zone.y,leveled_zone.z))
        var last_context: SpellContext?=null
        for(target in targets){
            val sub_context= SpellContext.at(target, context.power)
            last_context=decorated.use(sub_context) ?: last_context
        }
        sendParticleEffect(
            context.world,
            MagicParticleEffect(color, 0.2f),
            context.pos,
            AdvancedParticleMessage.CLOUD,
            leveled_zone.multiply(0.5),
            leveled_zone.x*leveled_zone.y*leveled_zone.z/6f
        )
        return last_context
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