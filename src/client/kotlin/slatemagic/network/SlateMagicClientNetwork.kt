package slatemagic.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler
import net.minecraft.world.World
import slatemagic.network.messages.AdvancedParticleMessage
import slatemagic.particle.AdvancedParticle

object SlateMagicClientNetwork {

    init{
        /* LINE */
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.LINE, particleHandler {
            AdvancedParticle.line(it, effect, from, to, speed, spreading_or_count)
        })
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.LIGHTNING, particleHandler {
            AdvancedParticle.lightning(it, effect, from, to, speed, spreading_or_count.toInt())
        })
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.CURVE, particleHandler {
            AdvancedParticle.curve(it, effect, from, to, speed, spreading_or_count)
        })

        /* BOX */
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.BOX, particleHandler {
            AdvancedParticle.box(it, effect, from, to, speed, spreading_or_count.toInt())
        })
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.CLOUD, particleHandler {
            AdvancedParticle.cloud(it, effect, from, to, speed, spreading_or_count.toInt())
        })

        /* BOOM AND MOVING ZONE*/
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.BOOM, particleHandler {
            AdvancedParticle.boom(it, effect, from, to, speed, spreading_or_count.toInt())
        })
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.CLOUD_BOOM, particleHandler {
            AdvancedParticle.cloud_boom(it, effect, from, to, speed, spreading_or_count.toInt())
        })
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.IMPLODE, particleHandler {
            AdvancedParticle.implode(it, effect, from, to, speed, spreading_or_count.toInt())
        })
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.SPIRAL, particleHandler {
            AdvancedParticle.spiral(it, effect, from, to, speed, spreading_or_count.toInt())
        })
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.STORM, particleHandler {
            AdvancedParticle.storm(it, effect, from, to, speed, spreading_or_count.toInt())
        })

        /* RING */
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.TORNADO, particleHandler {
            AdvancedParticle.tornado(it, effect, from, to, speed, spreading_or_count.toInt())
        })
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.RING, particleHandler {
            AdvancedParticle.ring(it, effect, from, to, speed, spreading_or_count.toInt())
        })
        ClientPlayNetworking.registerGlobalReceiver(AdvancedParticleMessage.SHOCKWAVE, particleHandler {
            AdvancedParticle.shockwave(it, effect, from, to, speed, spreading_or_count.toInt())
        })

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