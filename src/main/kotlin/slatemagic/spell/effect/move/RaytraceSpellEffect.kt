package slatemagic.spell.effect.move

import net.minecraft.client.util.math.Vector3d
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import slatemagic.network.messages.AdvancedParticleMessage
import slatemagic.network.messages.sendParticleEffect
import slatemagic.particle.MagicParticleEffect
import slatemagic.shape.SpellShape
import slatemagic.spell.SpellContext
import slatemagic.spell.effect.SpellEffect

class RaytraceSpellEffect(val maxDistance: Int, val decorated: SpellEffect): SpellEffect {

    override fun use(context: SpellContext): SpellContext? {
        val original=context.pos
        val actual=Vector3d(context.pos.x, context.pos.y, context.pos.z)
        val directionVector=Vec3d.fromPolar(context.direction.x,context.direction.y)
        val leveledMaxDistance= (maxDistance*(1.0+0.5*context.power)).toInt()
        for(i in 0..<leveledMaxDistance){
            val blockpos=BlockPos(actual.x,actual.y,actual.z)
            if(!context.world.getBlockState(blockpos).isAir){
                if(i!=0){
                    actual.x-=directionVector.x
                    actual.y-=directionVector.y
                    actual.z-=directionVector.z
                }
                break
            }
            actual.x+=directionVector.x
            actual.y+=directionVector.y
            actual.z+=directionVector.z
        }
        context.pos= Vec3d(actual.x, actual.y, actual.z)
        sendParticleEffect(
            context.world,
            MagicParticleEffect(color, 0.5f),
            original,
            AdvancedParticleMessage.CURVE,
            context.pos,
            4.0
        )
        decorated.use(context)
        return context
    }

    override val name: Text get() = Text.of("Air ").also { it.siblings.add(decorated.name) }

    override val description: Text get() = Text.of("from a distance of $maxDistance, ").also { it.siblings.add(decorated.description) }

    override val cost: Int get() = (decorated.cost*(1.0+maxDistance/10.0)).toInt()

    override val color: Vec3f get() = decorated.color.apply {
        add(0.15f,0.15f,0.2f)
        clamp(0.0f,1.0f)
    }

    override val shape: SpellShape get() = decorated.shape.also {
        it[0].repetition=(it[0].repetition+1+maxDistance/20).toByte()
        it[0].spacing=(it[0].spacing+4+maxDistance/5).toByte()
    }

}