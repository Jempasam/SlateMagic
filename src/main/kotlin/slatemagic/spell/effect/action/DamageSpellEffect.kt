package slatemagic.spell.effect.action

import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import slatemagic.helper.ColorTools
import slatemagic.network.messages.AdvancedParticleMessage
import slatemagic.network.messages.sendParticleEffect
import slatemagic.particle.MagicParticleEffect
import slatemagic.shape.SpellShape
import slatemagic.spell.SpellContext
import slatemagic.spell.build.NUMBER
import slatemagic.spell.build.SPELL
import slatemagic.spell.build.SpellNode
import slatemagic.spell.build.SpellPart
import slatemagic.spell.effect.SpellEffect
import kotlin.math.min

class DamageSpellEffect(val damage: Int): SpellEffect {

    override fun use(context: SpellContext): SpellContext? {
        val spawned=AreaEffectCloudEntity(EntityType.AREA_EFFECT_CLOUD, context.world)
        val living=context.entity
        if(living is LivingEntity){
            living.damage(DamageSource.MAGIC, damage.toFloat())
            sendParticleEffect(
                context.world,
                MagicParticleEffect(color, 0.5f),
                living.pos.add(0.0,0.5,0.0),
                AdvancedParticleMessage.BOOM,
                Vec3d(.2,.2,.2),
                damage*3.0
            )
            return context
        }
        else return null
    }

    override val name: Text get() = Text.of("Damage")

    override val description: Text get() = Text.of("inflict $damage damages")

    override val cost: Int get() = 5*damage

    override val color: Vec3f get() = ColorTools.vec(0xD80064).copy().apply {lerp(ColorTools.vec(0xD80034), min(1f,damage/20f))}

    override val shape: SpellShape get() = SpellShape(
        Array(4) {SpellShape.Circle(3, 0, 0, 0, 1, 0, 0)}
        .also {
            it[0]= SpellShape.Circle(16, 0, 0, 0, 1, 0, 0)
            it[2].spacing=60
            it[3]= SpellShape.Circle(16, 0, 0, 0, 2, 0, 0)
        }
    )

    object Node2: SpellNode<SpellEffect>{
        override val parameters = listOf<SpellPart.Type<*>>()
        override val name = "Damage 2"
        override fun build(parts: List<SpellPart<*>>): SpellPart<SpellEffect> {
            return SPELL.create(DamageSpellEffect(2))
        }
    }

    object Node4: SpellNode<SpellEffect>{
        override val parameters = listOf<SpellPart.Type<*>>()
        override val name = "Damage 4"
        override fun build(parts: List<SpellPart<*>>): SpellPart<SpellEffect> {
            return SPELL.create(DamageSpellEffect(4))
        }
    }

    object Node8: SpellNode<SpellEffect>{
        override val parameters = listOf<SpellPart.Type<*>>()
        override val name = "Damage 8"
        override fun build(parts: List<SpellPart<*>>): SpellPart<SpellEffect> {
            return SPELL.create(DamageSpellEffect(8))
        }
    }

    object NodeX: SpellNode<SpellEffect>{
        override val parameters = listOf(NUMBER)
        override val name = "Damage X"
        override fun build(parts: List<SpellPart<*>>): SpellPart<SpellEffect> {
            return SPELL.create(DamageSpellEffect(NUMBER.cast(parts[0])?.toInt() ?: 1))
        }
    }
}