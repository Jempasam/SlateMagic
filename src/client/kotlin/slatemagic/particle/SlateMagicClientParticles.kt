package slatemagic.particle


import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry


object SlateMagicClientParticles {
    init{
        ParticleFactoryRegistry.getInstance().apply {
            register(SlateMagicParticles.MAGIC) { it -> MagicParticle.Factory(it) }
            register(SlateMagicParticles.SPELL_CIRCLE) { it -> SpellCircleParticle.Factory(it) }
            register(SlateMagicParticles.SPELL_SPHERE) { it -> SpellSphereParticle.Factory(it) }
            register(SlateMagicParticles.SPELL_CROSSED) { it -> SpellCrossedParticle.Factory(it) }
            register(SlateMagicParticles.SPELL_CIRCLE_MOVING) { it -> SpellCircleMovingParticle.Factory(it) }
        }
    }
}