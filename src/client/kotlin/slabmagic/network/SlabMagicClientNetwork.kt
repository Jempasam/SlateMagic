package slabmagic.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayPayloadHandler
import net.minecraft.client.MinecraftClient
import net.minecraft.network.packet.CustomPayload
import net.minecraft.world.World
import slabmagic.network.messages.AdvancedParticleMessage
import slabmagic.network.messages.AdvancedParticleMessage.Shape.*
import slabmagic.network.messages.PlayerMovementMessage
import slabmagic.particle.AdvancedParticle

object SlabMagicClientNetwork {

    init{
        /* LINE */
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.ID, particleHandler {
            when(shape){
                LINE -> AdvancedParticle.line(it, effect, from, to, speed, spreadingOrCount)
                LIGHTNING -> AdvancedParticle.lightning(it, effect, from, to, speed, spreadingOrCount.toInt())
                CURVE -> AdvancedParticle.curve(it, effect, from, to, speed, spreadingOrCount)

                BOX -> AdvancedParticle.box(it, effect, from, to, speed, spreadingOrCount.toInt())
                CLOUD -> AdvancedParticle.cloud(it, effect, from, to, speed, spreadingOrCount.toInt())

                BOOM -> AdvancedParticle.boom(it, effect, from, to, speed, spreadingOrCount.toInt())
                CLOUD_BOOM -> AdvancedParticle.cloudboom(it, effect, from, to, speed, spreadingOrCount.toInt())
                IMPLODE -> AdvancedParticle.implode(it, effect, from, to, speed, spreadingOrCount.toInt())
                SPIRAL -> AdvancedParticle.spiral(it, effect, from, to, speed, spreadingOrCount.toInt())
                STORM -> AdvancedParticle.storm(it, effect, from, to, speed, spreadingOrCount.toInt())

                TORNADO -> AdvancedParticle.tornado(it, effect, from, to, speed, spreadingOrCount.toInt())
                RING -> AdvancedParticle.ring(it, effect, from, to, speed, spreadingOrCount.toInt())
                SHOCKWAVE -> AdvancedParticle.shockwave(it, effect, from, to, speed, spreadingOrCount.toInt())
            }
        })
        /* MOVEMENT */
        ClientPlayNetworking.registerGlobalReceiver(PlayerMovementMessage.ID, handler{ message ->
            MinecraftClient.getInstance().player?.apply {
                velocity=message.velocity
                velocityDirty=true
            }
        })

    }

    private fun <T: CustomPayload> handler(function: (T)->Unit): PlayPayloadHandler<T>{
        return PlayPayloadHandler<T> { message, context ->
            context.client().execute{ function(message) }
        }
    }

    private fun particleHandler(function: AdvancedParticleMessage.(World)->Unit): PlayPayloadHandler<AdvancedParticleMessage>{
        return PlayPayloadHandler { message, context ->
            if(message.world == context.client().world?.registryKey){
                context.client().execute{ message.function(context.client().world!!) }
            }
        }
    }
}