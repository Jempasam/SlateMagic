package slatemagic.spell.action

import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import slatemagic.helper.ColorTools
import slatemagic.network.messages.AdvancedParticleMessage
import slatemagic.network.messages.sendParticleEffect
import slatemagic.particle.MagicParticleEffect
import slatemagic.shape.SpellShape
import slatemagic.spell.Spell
import slatemagic.spell.SpellContext
import kotlin.math.sqrt

class PotionSpell(val effect: StatusEffect): Spell {

    override fun use(context: SpellContext): SpellContext? {
        val spawned=AreaEffectCloudEntity(EntityType.AREA_EFFECT_CLOUD, context.world)
        val living=context.entity
        if(living is LivingEntity){
            val power_sqrt= sqrt(context.power.toDouble())
            living.addStatusEffect(StatusEffectInstance(effect, (30*power_sqrt*2).toInt(), (power_sqrt/2).toInt()))
            sendParticleEffect(
                context.world,
                MagicParticleEffect(color, 0.5f),
                living.pos.add(0.0,0.5,0.0),
                AdvancedParticleMessage.SPIRAL,
                Vec3d(1.5,1.5,1.5),
                40.0+10.0*context.power
            )
            return context
        }
        else return null
    }

    override val name: Text get() = effect.name

    override val description: Text get() = Text.of("inflict an effect of ").apply { siblings.add(effect.name) }

    override val cost: Int get() = 10

    override val color: Vec3f get() = ColorTools.vec(ColorTools.of(effect))

    override val shape: SpellShape get() = SpellShape(
        Array(4) {SpellShape.Circle(3, 0, 0, 0, 1, 0, 0)}
        .also {
            it[0]= SpellShape.Circle(16, 0, 0, 0, 1, 0, 0)
            it[2].spacing=60
            it[3]= SpellShape.Circle(16, 0, 0, 0, 2, 0, 0)
        }
    )

}