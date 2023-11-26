package slatemagic.spell.effect.move

import net.minecraft.client.util.math.Vector3d
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import slatemagic.network.messages.AdvancedParticleMessage
import slatemagic.network.messages.sendParticleEffect
import slatemagic.particle.MagicParticleEffect
import slatemagic.spell.SpellContext
import slatemagic.spell.effect.SpellEffect

class PierceSpellEffect(val decorated: SpellEffect): SpellEffect by decorated {

    override fun use(context: SpellContext): SpellContext? {
        val original=context.pos
        val actual=Vector3d(context.pos.x, context.pos.y, context.pos.z)
        val directionVector=Vec3d.fromPolar(context.direction.x,context.direction.y).multiply(0.05)
        val leveledMaxDistance= 20
        for(i in 0..<leveledMaxDistance){
            val blockpos=BlockPos(actual.x,actual.y,actual.z)
            if(!context.world.getBlockState(blockpos).isAir){
                break
            }
            actual.x+=directionVector.x
            actual.y+=directionVector.y
            actual.z+=directionVector.z
        }
        sendParticleEffect(
            context.world,
            MagicParticleEffect(color, 0.3f),
            original,
            AdvancedParticleMessage.BOOM,
            Vec3d(0.1,0.1,0.1),
            6.0,
            speed=directionVector.negate()
        )
        context.pos= Vec3d(actual.x, actual.y, actual.z)
        decorated.use(context)
        return context
    }

    override val name: Text get() = Text.of("Intra-").also { it.siblings.add(decorated.name) }

    override val description: Text get() = Text.of("in block, ").also { it.siblings.add(decorated.description) }

}