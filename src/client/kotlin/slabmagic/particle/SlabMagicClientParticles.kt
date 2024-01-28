package slabmagic.particle


import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry


object SlabMagicClientParticles {
    init{
        ParticleFactoryRegistry.getInstance().apply {
            register(SlabMagicParticles.MAGIC) { it -> MagicParticle.Factory(it) }
            register(SlabMagicParticles.MAGIC_EARTH) { it -> MagicParticle.Factory(it) }
            register(SlabMagicParticles.MAGIC_FIRE) { it -> MagicParticle.Factory(it) }
            register(SlabMagicParticles.MAGIC_LIGHT) { it -> MagicParticle.Factory(it) }
            register(SlabMagicParticles.MAGIC_ELECTRIC) { it -> MagicParticle.Factory(it) }

            register(SlabMagicParticles.CUBE) { it -> EnergyBlockParticle.Factory(it) }
            register(SlabMagicParticles.CUBE_EARTH) { it -> EnergyBlockParticle.Factory(it) }
            register(SlabMagicParticles.CUBE_FIRE) { it -> EnergyBlockParticle.Factory(it) }
            register(SlabMagicParticles.CUBE_LIGHT) { it -> EnergyBlockParticle.Factory(it) }
            register(SlabMagicParticles.CUBE_ELECTRIC) { it -> EnergyBlockParticle.Factory(it) }

            register(SlabMagicParticles.SPELL_CIRCLE) { it -> SpellCircleParticle.Factory(it) }
            register(SlabMagicParticles.SPELL_SPHERE) { it -> SpellSphereParticle.Factory(it) }
            register(SlabMagicParticles.SPELL_CROSSED) { it -> SpellCrossedParticle.Factory(it) }
            register(SlabMagicParticles.SPELL_CIRCLE_MOVING) { it -> SpellCircleMovingParticle.Factory(it) }

        }
    }
}