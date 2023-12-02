package slabmagic.network.messages

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d
import slabmagic.SlabMagicMod
import slabmagic.network.NetworkMessage
import slabmagic.network.ServerNetwork

class PlayerMovementMessage(val velocity: Vec3d): NetworkMessage {

    constructor(packet: PacketByteBuf): this(
        Vec3d(packet.readDouble(), packet.readDouble(), packet.readDouble())
    )

    override fun write(): PacketByteBuf{
        val buf = PacketByteBufs.create()
        buf.writeDouble(velocity.x)
        buf.writeDouble(velocity.y)
        buf.writeDouble(velocity.z)
        return buf
    }

    companion object{
        val ID=SlabMagicMod.id("push_player")
    }
}

fun ServerPlayerEntity.push(x: Double, y: Double, z: Double){
    addVelocity(x, y, z)
    ServerNetwork.sendTo(this, PlayerMovementMessage.ID, PlayerMovementMessage(velocity))
}