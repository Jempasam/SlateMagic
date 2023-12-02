package slabmagic.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler
import net.minecraft.client.MinecraftClient
import net.minecraft.network.PacketByteBuf
import net.minecraft.world.World
import slabmagic.network.messages.AdvancedParticleMessage
import slabmagic.network.messages.PlayerMovementMessage
import slabmagic.particle.AdvancedParticle

object SlabMagicClientNetwork {

    init{
        /* LINE */
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.LINE, particleHandler {
            AdvancedParticle.line(it, effect, from, to, speed, spreadingOrCount)
        })
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.LIGHTNING, particleHandler {
            AdvancedParticle.lightning(it, effect, from, to, speed, spreadingOrCount.toInt())
        })
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.CURVE, particleHandler {
            AdvancedParticle.curve(it, effect, from, to, speed, spreadingOrCount)
        })

        /* BOX */
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.BOX, particleHandler {
            AdvancedParticle.box(it, effect, from, to, speed, spreadingOrCount.toInt())
        })
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.CLOUD, particleHandler {
            AdvancedParticle.cloud(it, effect, from, to, speed, spreadingOrCount.toInt())
        })

        /* BOOM AND MOVING ZONE*/
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.BOOM, particleHandler {
            AdvancedParticle.boom(it, effect, from, to, speed, spreadingOrCount.toInt())
        })
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.CLOUD_BOOM, particleHandler {
            AdvancedParticle.cloudboom(it, effect, from, to, speed, spreadingOrCount.toInt())
        })
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.IMPLODE, particleHandler {
            AdvancedParticle.implode(it, effect, from, to, speed, spreadingOrCount.toInt())
        })
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.SPIRAL, particleHandler {
            AdvancedParticle.spiral(it, effect, from, to, speed, spreadingOrCount.toInt())
        })
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.STORM, particleHandler {
            AdvancedParticle.storm(it, effect, from, to, speed, spreadingOrCount.toInt())
        })

        /* RING */
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.TORNADO, particleHandler {
            AdvancedParticle.tornado(it, effect, from, to, speed, spreadingOrCount.toInt())
        })
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.RING, particleHandler {
            AdvancedParticle.ring(it, effect, from, to, speed, spreadingOrCount.toInt())
        })
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.SHOCKWAVE, particleHandler {
            AdvancedParticle.shockwave(it, effect, from, to, speed, spreadingOrCount.toInt())
        })

        /* MOVEMENT */
        ClientPlayNetworking.registerGlobalReceiver(PlayerMovementMessage.ID, handler(::PlayerMovementMessage) {
            MinecraftClient.getInstance().player?.apply {
                velocity=it.velocity
                velocityDirty=true
            }
        })

    }

    private inline fun <T> handler(crossinline const: (PacketByteBuf)->T, crossinline function: (T)->Unit): PlayChannelHandler{
        return PlayChannelHandler { client, handler, buf, _ ->
            val message=const(buf)
            client.execute{ function(message) }
        }
    }

    private inline fun particleHandler(crossinline function: AdvancedParticleMessage.(World)->Unit): PlayChannelHandler{
        return PlayChannelHandler { client, handler, buf, _ ->
            val message=AdvancedParticleMessage(buf)
            if(message.world == handler.world.registryKey){
                client.execute{ message.function(handler.world) }
            }
        }
    }
}