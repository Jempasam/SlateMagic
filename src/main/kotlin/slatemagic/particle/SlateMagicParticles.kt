package slatemagic.particle

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.particle.ParticleType
import net.minecraft.util.registry.Registry
import slatemagic.SlateMagicMod

object SlateMagicParticles {

    val MAGIC= register("magic", FabricParticleTypes.complex(false, MagicParticleEffect.Factory))

    val SPELL_CIRCLE= register("spell_circle", FabricParticleTypes.complex(false, SpellCircleParticleEffect.Factory))

    val SPELL_SPHERE= register("spell_sphere", FabricParticleTypes.complex(false, SpellCircleParticleEffect.Factory))

    val SPELL_CROSSED= register("spell_crossed", FabricParticleTypes.complex(false, SpellCircleParticleEffect.Factory))

    private fun <T: ParticleType<*>>register(id: String, type: T): T{
        Registry.register(Registry.PARTICLE_TYPE, SlateMagicMod.id(id), type)
        return type
    }
}