package slabmagic.spell.effect.action

import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.text.Text
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
import kotlin.math.min

class DamageSpellEffect(val damage: Int): SpellEffect {

    override fun use(context: SpellContext): SpellContext? {
        val spawned=AreaEffectCloudEntity(EntityType.AREA_EFFECT_CLOUD, context.world)
        val living=context.entity
        return if(living is LivingEntity){
            living.damage(context.world.damageSources.magic(), damage.toFloat())
            sendParticleEffect(
                context.world,
                MagicParticleEffect(color, 0.5f),
                living.pos.add(0.0,0.5,0.0),
                AdvancedParticleMessage.Shape.BOOM,
                Vec3d(.2,.2,.2),
                damage*3.0
            )
            context
        } else null
    }

    override val name: Text get() = spellName("damage")
    override val description: Text get() = spellDesc("damage",damage)

    override val cost: Int get() = 5*damage

    override val color: Vector3f get() = ColorTools.vec(0xD80064).lerp(ColorTools.vec(0xD80034), min(1f,damage/20f))

    override val shape: SpellShape get() = SpellShape(
        Array(4) {SpellShape.Circle(3, 0, 0, 0, 1, 0, 0)}
        .also {
            it[0]= SpellShape.Circle(16, 0, 0, 0, 1, 0, 0)
            it[2].spacing=60
            it[3]= SpellShape.Circle(16, 0, 0, 0, 2, 0, 0)
        }
    )
}