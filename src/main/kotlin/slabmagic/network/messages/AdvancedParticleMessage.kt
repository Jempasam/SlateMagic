package slabmagic.network.messages

import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import slabmagic.SlabMagicMod
import slabmagic.network.ServerNetwork
import slabmagic.utils.PacketCodec2

data class AdvancedParticleMessage(val effect: ParticleEffect, val shape: Shape, val world: RegistryKey<World>, val from: Vec3d, val to: Vec3d, val speed: Vec3d,val spreadingOrCount: Double): CustomPayload{
    companion object{
        val ID=CustomPayload.id<AdvancedParticleMessage>(SlabMagicMod.id("advanced_particle").toString())

        val CODEC=PacketCodec2.tuple(
            ParticleTypes.PACKET_CODEC, {it.effect},
            PacketCodecs.codec(StringIdentifiable.createCodec{Shape.entries.toTypedArray()}), {it.shape},
            RegistryKey.createPacketCodec(RegistryKeys.WORLD), {it.world},
            PacketCodecs.codec(Vec3d.CODEC), {it.from},
            PacketCodecs.codec(Vec3d.CODEC), {it.to},
            PacketCodecs.codec(Vec3d.CODEC), {it.speed},
            PacketCodecs.DOUBLE, {it.spreadingOrCount},
            ::AdvancedParticleMessage
        )
    }

    override fun getId() = ID

    enum class Shape: StringIdentifiable{
        LINE, CURVE, LIGHTNING,
        IMPLODE, BOOM, CLOUD_BOOM, STORM, SPIRAL,
        BOX, CLOUD,
        TORNADO, RING, SHOCKWAVE;
        override fun asString() = this.name
    }
}

fun sendParticleEffect(world: ServerWorld, effect: ParticleEffect, pos: Vec3d, shape: AdvancedParticleMessage.Shape=AdvancedParticleMessage.Shape.BOX, size: Vec3d=Vec3d(0.02,0.02,0.02), count: Double=1.0, speed: Vec3d=Vec3d.ZERO){
    ServerNetwork.sendToAll(
        world,
        AdvancedParticleMessage( effect, shape, world.registryKey, pos, size, speed, count )
    )
}

fun sendSimpleParticleEffect(world: ServerWorld, effect: ParticleEffect, pos: Vec3d, size: Vec3d=Vec3d(0.02,0.02,0.02), count: Int=1, speed: Double=0.0){
    world.spawnParticles(
        effect,
        pos.x, pos.y, pos.z,
        count,
        size.x, size.y, size.z,
        speed
    )
}