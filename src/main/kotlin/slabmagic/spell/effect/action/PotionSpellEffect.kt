package slabmagic.spell.effect.action

import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f
import slabmagic.helper.ColorTools
import slabmagic.network.messages.AdvancedParticleMessage
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.MagicParticleEffect
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect
import slabmagic.spell.spellDesc
import slabmagic.spell.spellName
import kotlin.math.sqrt

class PotionSpellEffect(val effect: RegistryEntry<StatusEffect>, val duration: Int, val amplifier: Int): SpellEffect {

    override fun use(context: SpellContext): SpellContext? {
        val spawned=AreaEffectCloudEntity(EntityType.AREA_EFFECT_CLOUD, context.world)
        val living=context.entity
        return if(living is LivingEntity){
            val powerSqrt= sqrt(context.stored.power.toDouble())
            val leveledDuration=(duration*powerSqrt).toInt()
            val leveledAmplifier=((amplifier+1)*powerSqrt).toInt()-1
            living.addStatusEffect(StatusEffectInstance(effect, duration, amplifier))
            sendParticleEffect(
                context.world,
                MagicParticleEffect(color, 0.5f),
                living.pos.add(0.0,0.5,0.0),
                AdvancedParticleMessage.Shape.SPIRAL,
                Vec3d(1.5,1.5,1.5),
                40.0+10.0*context.stored.power
            )
            context
        } else null
    }

    override val name get() = spellName("give_potion",effect.value().name)

    override val description get() = spellDesc("give_potion", duration/20.0, effect.value().name, amplifier+1)

    override val cost: Int get() = duration*(amplifier+1)/10

    override val color: Vector3f get() = ColorTools.vec(ColorTools.of(effect.value()))

    override val shape: SpellShape get() = SpellShape(
        Array(4) {SpellShape.Circle(3, 0, 0, 0, 1, 0, 0)}
        .also {
            it[0]= SpellShape.Circle(16, 0, 0, 0, 1, 0, 0)
            it[2].spacing=(60+duration/5).toByte()
            it[3]= SpellShape.Circle(16, 0, 0, 0, 2, 0, 0)
        }
    )

}