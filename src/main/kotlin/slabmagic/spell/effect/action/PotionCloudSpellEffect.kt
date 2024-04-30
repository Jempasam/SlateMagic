package slabmagic.spell.effect.action

import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.registry.entry.RegistryEntry
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

class PotionCloudSpellEffect(val effect: RegistryEntry<StatusEffect>): SpellEffect {

    override fun use(context: SpellContext): SpellContext {
        val spawned=AreaEffectCloudEntity(EntityType.AREA_EFFECT_CLOUD, context.world)
        spawned.addEffect(StatusEffectInstance(effect, 20*5, 0))
        spawned.setPosition(context.pos)
        context.world.spawnEntity(spawned)
        sendParticleEffect(
            context.world,
            MagicParticleEffect(color, 0.5f),
            context.pos,
            AdvancedParticleMessage.Shape.SPIRAL,
            Vec3d(1.5,0.5,1.5),
            40.0
        )
        return SpellContext.at(spawned, context.stored)
    }

    override val name: Text get() = spellName("potion_cloud", effect.value().name)

    override val description: Text get() = spellDesc("potion_cloud", effect.value().name)

    override val cost: Int get() = 10

    override val color: Vector3f get() = ColorTools.vec(ColorTools.of(effect.value()))

    override val shape: SpellShape get() = SpellShape(
        Array(4) {SpellShape.Circle(3, 0, 0, 0, 1, 0, 0)}
        .also {
            it[0]= SpellShape.Circle(16, 0, 0, 0, 1, 0, 0)
            it[2].spacing=60
            it[3]= SpellShape.Circle(16, 0, 0, 0, 2, 0, 0)
        }
    )

}