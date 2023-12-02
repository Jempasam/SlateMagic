package slabmagic.particle


import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry


object SlabMagicClientParticles {
    init{
        ParticleFactoryRegistry.getInstance().apply {
            register(SlabMagicParticles.MAGIC) { it -> MagicParticle.Factory(it) }
            register(SlabMagicParticles.SPELL_CIRCLE) { it -> SpellCircleParticle.Factory(it) }
            register(SlabMagicParticles.SPELL_SPHERE) { it -> SpellSphereParticle.Factory(it) }
            register(SlabMagicParticles.SPELL_CROSSED) { it -> SpellCrossedParticle.Factory(it) }
            register(SlabMagicParticles.SPELL_CIRCLE_MOVING) { it -> SpellCircleMovingParticle.Factory(it) }
            register(SlabMagicParticles.ENERGY_BLOCK) { it -> EnergyBlockParticle.Factory(it) }
        }
    }
}