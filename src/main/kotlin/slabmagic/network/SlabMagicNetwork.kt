package slabmagic.network

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import slabmagic.network.messages.AdvancedParticleMessage
import slabmagic.network.messages.PlayerMovementMessage

object SlabMagicNetwork {

    val ADVANCED_PARTICLES=PayloadTypeRegistry.playS2C().register(AdvancedParticleMessage.ID, AdvancedParticleMessage.CODEC)

    val PLAYER_MOVEMENT=PayloadTypeRegistry.playS2C().register(PlayerMovementMessage.ID, PlayerMovementMessage.CODEC)
    
}