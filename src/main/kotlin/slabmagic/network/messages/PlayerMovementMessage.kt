package slabmagic.network.messages

import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d
import slabmagic.SlabMagicMod
import slabmagic.network.ServerNetwork

class PlayerMovementMessage(val velocity: Vec3d): CustomPayload {
    companion object{
        val ID=CustomPayload.id<PlayerMovementMessage>(SlabMagicMod.id("push_player").toString())
        val CODEC= PacketCodecs.codec(Vec3d.CODEC).xmap(::PlayerMovementMessage,{it.velocity})
    }

    override fun getId() = ID
}

fun ServerPlayerEntity.push(x: Double, y: Double, z: Double){
    addVelocity(x, y, z)
    ServerNetwork.sendTo(this, PlayerMovementMessage(velocity))
}