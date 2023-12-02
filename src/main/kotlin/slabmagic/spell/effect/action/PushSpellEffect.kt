package slabmagic.spell.effect.action

import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import slabmagic.helper.ColorTools
import slabmagic.network.messages.AdvancedParticleMessage
import slabmagic.network.messages.push
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.MagicParticleEffect
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class PushSpellEffect(val strength: Float): SpellEffect {

    override fun use(context: SpellContext): SpellContext? {
        val spawned=AreaEffectCloudEntity(EntityType.AREA_EFFECT_CLOUD, context.world)
        val living=context.entity
        if(living is LivingEntity){
            val pitch=context.direction.x
            val yaw=context.direction.y
            val f = -sin(yaw * 0.017453292) * cos(pitch * 0.017453292) * strength
            val g = -sin(pitch * 0.017453292) * strength
            val h = cos(yaw * 0.017453292) * cos(pitch * 0.017453292) *strength
            if(living is ServerPlayerEntity) living.push(f, g, h)
            else living.addVelocity(f, g, h)
            sendParticleEffect(
                context.world,
                MagicParticleEffect(color, 0.5f),
                living.pos.add(0.0,0.5,0.0),
                AdvancedParticleMessage.BOOM,
                Vec3d(.2,.2,.2),
                strength*3.0
            )
            return context
        }
        else return null
    }

    override val name: Text get() = Text.of("Push")

    override val description: Text get() = Text.of("push with a strength of $strength")

    override val cost: Int get() = (5*strength).toInt()

    override val color: Vec3f get() = ColorTools.vec(0xBED3FF).copy().apply {lerp(ColorTools.vec(0xDFF5FF), min(1f,strength))}

    override val shape: SpellShape get() = SpellShape(
        Array(4) {SpellShape.Circle(3, 30, 0, 0, 1, -20, 1)}
        .also {
            for(i in arrayOf(1,3))
                it[i]= SpellShape.Circle(6, 30, 0, 0, 1, 10, 3)
        }
    )
}