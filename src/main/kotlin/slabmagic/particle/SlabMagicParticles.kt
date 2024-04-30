package slabmagic.particle

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.particle.ParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import slabmagic.SlabMagicMod

object SlabMagicParticles {

    val MAGIC= register("simple_magic", FabricParticleTypes.complex(false, MagicParticleEffect::codecFor, MagicParticleEffect::packetCodecFor))

    val MAGIC_ELECTRIC= register("simple_electric", FabricParticleTypes.complex(false, MagicParticleEffect::codecFor, MagicParticleEffect::packetCodecFor))

    val MAGIC_FIRE= register("simple_fire", FabricParticleTypes.complex(false, MagicParticleEffect::codecFor, MagicParticleEffect::packetCodecFor))

    val MAGIC_EARTH= register("simple_earth", FabricParticleTypes.complex(false, MagicParticleEffect::codecFor, MagicParticleEffect::packetCodecFor))

    val MAGIC_LIGHT= register("simple_light", FabricParticleTypes.complex(false, MagicParticleEffect::codecFor, MagicParticleEffect::packetCodecFor))


    val CUBE= register("cube_magic", FabricParticleTypes.complex(false, EnergyBlockParticleEffect::codecFor, EnergyBlockParticleEffect::packetCodecFor))

    val CUBE_ELECTRIC= register("cube_electric", FabricParticleTypes.complex(false, EnergyBlockParticleEffect::codecFor, EnergyBlockParticleEffect::packetCodecFor))

    val CUBE_FIRE= register("cube_fire", FabricParticleTypes.complex(false, EnergyBlockParticleEffect::codecFor, EnergyBlockParticleEffect::packetCodecFor))

    val CUBE_EARTH= register("cube_earth", FabricParticleTypes.complex(false, EnergyBlockParticleEffect::codecFor, EnergyBlockParticleEffect::packetCodecFor))

    val CUBE_LIGHT= register("cube_light", FabricParticleTypes.complex(false, EnergyBlockParticleEffect::codecFor, EnergyBlockParticleEffect::packetCodecFor))


    val SPELL_CIRCLE= register("spell_circle", FabricParticleTypes.complex(false, SpellCircleParticleEffect::codecFor, SpellCircleParticleEffect::packetCodecFor))

    val SPELL_SPHERE= register("spell_sphere", FabricParticleTypes.complex(false, SpellCircleParticleEffect::codecFor, SpellCircleParticleEffect::packetCodecFor))

    val SPELL_CIRCLE_MOVING= register("spell_circle_moving", FabricParticleTypes.complex(false, SpellCircleParticleEffect::codecFor, SpellCircleParticleEffect::packetCodecFor))

    val SPELL_CROSSED= register("spell_crossed", FabricParticleTypes.complex(false, SpellCircleParticleEffect::codecFor, SpellCircleParticleEffect::packetCodecFor))

    private fun <T: ParticleType<*>>register(id: String, type: T): T{
        Registry.register(Registries.PARTICLE_TYPE, SlabMagicMod.id(id), type)
        return type
    }
}