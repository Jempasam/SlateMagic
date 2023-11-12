package slatemagic.network.messages

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.World
import slatemagic.SlateMagicMod
import slatemagic.network.NetworkMessage
import slatemagic.network.ServerNetwork

class AdvancedParticleMessage(val effect: ParticleEffect, val world: RegistryKey<World>, val from: Vec3d, val to: Vec3d, val speed: Vec3d,val spreadingOrCount: Double):
    NetworkMessage {

    constructor(packet: PacketByteBuf): this(
        (Registry.PARTICLE_TYPE.get(Identifier(packet.readString()))) ?.let{ create(it,packet) } ?: ParticleTypes.CRIT,
        RegistryKey.of(Registry.WORLD_KEY, Identifier(packet.readString())),
        Vec3d(packet.readDouble(), packet.readDouble(), packet.readDouble()),
        Vec3d(packet.readDouble(), packet.readDouble(), packet.readDouble()),
        Vec3d(packet.readDouble(), packet.readDouble(), packet.readDouble()),
        packet.readDouble()
    )

    override fun write(): PacketByteBuf{
        val buf = PacketByteBufs.create()
        buf.writeIdentifier(Registry.PARTICLE_TYPE.getId(effect.type))
        effect.write(buf)
        buf.writeString(world.value.toString())
        buf.writeDouble(from.x)
        buf.writeDouble(from.y)
        buf.writeDouble(from.z)
        buf.writeDouble(to.x)
        buf.writeDouble(to.y)
        buf.writeDouble(to.z)
        buf.writeDouble(speed.x)
        buf.writeDouble(speed.y)
        buf.writeDouble(speed.z)
        buf.writeDouble(spreadingOrCount)
        return buf
    }

    companion object{

        private fun <T: ParticleEffect> create(type: ParticleType<T>, buffer: PacketByteBuf): T{
            return type.parametersFactory.read(type, buffer)
        }

        val LINE=SlateMagicMod.id("particle_line")
        val CURVE=SlateMagicMod.id("particle_curve")
        val LIGHTNING=SlateMagicMod.id("particle_lightning")

        val IMPLODE=SlateMagicMod.id("particle_implode")
        val BOOM=SlateMagicMod.id("particle_boom")
        val CLOUD_BOOM=SlateMagicMod.id("particle_cloud_boom")
        val STORM=SlateMagicMod.id("particle_storm")
        val SPIRAL=SlateMagicMod.id("particle_spiral")

        val BOX=SlateMagicMod.id("particle_box")
        val CLOUD=SlateMagicMod.id("particle_cloud")

        val TORNADO=SlateMagicMod.id("particle_tornado")
        val RING=SlateMagicMod.id("particle_ring")
        val SHOCKWAVE=SlateMagicMod.id("particle_shockwave")


        val IDS= setOf(
            LINE, CURVE, LIGHTNING,
            IMPLODE, BOOM, CLOUD_BOOM, STORM, SPIRAL,
            BOX, CLOUD,
            TORNADO, RING, SHOCKWAVE
        )
    }
}

fun sendParticleEffect(world: ServerWorld, effect: ParticleEffect, pos: Vec3d, id: Identifier= AdvancedParticleMessage.BOX, size: Vec3d=Vec3d(0.1,0.1,0.1), count: Double=1.0, speed: Vec3d=Vec3d.ZERO){
    ServerNetwork.sendToAll(
        world, id,
        AdvancedParticleMessage( effect, world.registryKey, pos, size, speed, count )
    )
}