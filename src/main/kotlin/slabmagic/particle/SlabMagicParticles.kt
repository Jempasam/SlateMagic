package slabmagic.particle

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.particle.ParticleType
import net.minecraft.util.registry.Registry
import slabmagic.SlabMagicMod

object SlabMagicParticles {

    val MAGIC= register("magic", FabricParticleTypes.complex(false, MagicParticleEffect.Factory))

    val SPELL_CIRCLE= register("spell_circle", FabricParticleTypes.complex(false, SpellCircleParticleEffect.Factory))

    val SPELL_SPHERE= register("spell_sphere", FabricParticleTypes.complex(false, SpellCircleParticleEffect.Factory))

    val SPELL_CIRCLE_MOVING= register("spell_circle_moving", FabricParticleTypes.complex(false, SpellCircleParticleEffect.Factory))

    val SPELL_CROSSED= register("spell_crossed", FabricParticleTypes.complex(false, SpellCircleParticleEffect.Factory))

    val ENERGY_BLOCK= register("energy_block", FabricParticleTypes.complex(false, EnergyBlockParticleEffect.Factory))

    private fun <T: ParticleType<*>>register(id: String, type: T): T{
        Registry.register(Registry.PARTICLE_TYPE, SlabMagicMod.id(id), type)
        return type
    }
}