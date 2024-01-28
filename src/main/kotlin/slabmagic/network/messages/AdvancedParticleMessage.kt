package slabmagic.network.messages

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
import slabmagic.SlabMagicMod
import slabmagic.network.NetworkMessage
import slabmagic.network.ServerNetwork

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

        val LINE=SlabMagicMod.id("particle_line")
        val CURVE=SlabMagicMod.id("particle_curve")
        val LIGHTNING=SlabMagicMod.id("particle_lightning")

        val IMPLODE=SlabMagicMod.id("particle_implode")
        val BOOM=SlabMagicMod.id("particle_boom")
        val CLOUD_BOOM=SlabMagicMod.id("particle_cloud_boom")
        val STORM=SlabMagicMod.id("particle_storm")
        val SPIRAL=SlabMagicMod.id("particle_spiral")

        val BOX=SlabMagicMod.id("particle_box")
        val CLOUD=SlabMagicMod.id("particle_cloud")

        val TORNADO=SlabMagicMod.id("particle_tornado")
        val RING=SlabMagicMod.id("particle_ring")
        val SHOCKWAVE=SlabMagicMod.id("particle_shockwave")


        val IDS= setOf(
            LINE, CURVE, LIGHTNING,
            IMPLODE, BOOM, CLOUD_BOOM, STORM, SPIRAL,
            BOX, CLOUD,
            TORNADO, RING, SHOCKWAVE
        )
    }
}

fun sendParticleEffect(world: ServerWorld, effect: ParticleEffect, pos: Vec3d, id: Identifier= AdvancedParticleMessage.BOX, size: Vec3d=Vec3d(0.02,0.02,0.02), count: Double=1.0, speed: Vec3d=Vec3d.ZERO){
    ServerNetwork.sendToAll(
        world, id,
        AdvancedParticleMessage( effect, world.registryKey, pos, size, speed, count )
    )
}

fun sendSimpleParticleEffect(world: ServerWorld, effect: ParticleEffect, pos: Vec3d, id: Identifier= AdvancedParticleMessage.BOX, size: Vec3d=Vec3d(0.02,0.02,0.02), count: Int=1, speed: Double=0.0){
    world.spawnParticles(
        effect,
        pos.x, pos.y, pos.z,
        count,
        size.x, size.y, size.z,
        speed
    )
}