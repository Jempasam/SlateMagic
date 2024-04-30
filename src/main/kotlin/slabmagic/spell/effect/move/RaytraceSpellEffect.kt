package slabmagic.spell.effect.move

import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import org.joml.Vector3d
import org.joml.Vector3f
import slabmagic.network.messages.AdvancedParticleMessage
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.MagicParticleEffect
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect
import slabmagic.utils.coerceIn

class RaytraceSpellEffect(val maxDistance: Int, val decorated: SpellEffect): SpellEffect {

    override fun use(context: SpellContext): SpellContext {
        val original=context.pos
        val actual=Vector3d(context.pos.x, context.pos.y, context.pos.z)
        val directionVector=Vec3d.fromPolar(context.direction.x,context.direction.y).multiply(0.333)
        val leveledMaxDistance= (maxDistance*(1.0+0.5*context.stored.power)).toInt()*3
        for(i in 0..<leveledMaxDistance){
            val blockpos=BlockPos.ofFloored(actual.x,actual.y,actual.z)
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
            AdvancedParticleMessage.Shape.CURVE,
            context.pos,
            4.0
        )
        decorated.use(context)
        return context
    }

    override val name: Text get() = Text.literal("Air ").append(decorated.name)

    override val description: Text get() = Text.literal("from a distance of $maxDistance, ").append(decorated.description)

    override val cost: Int get() = (decorated.cost*(1.0+maxDistance/10.0)).toInt()

    override val color: Vector3f get() = decorated.color.apply {
        add(0.15f,0.15f,0.2f)
        coerceIn(0.0f,1.0f)
    }

    override val shape: SpellShape get() = decorated.shape.also {
        it[0].repetition=(it[0].repetition+1+maxDistance/20).toByte()
        it[0].spacing=(it[0].spacing+4+maxDistance/5).toByte()
    }

}