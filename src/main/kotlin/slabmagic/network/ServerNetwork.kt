package slabmagic.network

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier

object ServerNetwork {

    fun sendTo(player: ServerPlayerEntity, id: Identifier, message: NetworkMessage){
        ServerPlayNetworking.send(player, id, message.write())
    }

    fun sendToAll(players: Iterable<ServerPlayerEntity>, id: Identifier, message: NetworkMessage){
        val buffer=message.write()
        for(player in players) ServerPlayNetworking.send(player, id, buffer)
    }

    fun sendToAll(server: MinecraftServer, id: Identifier, message: NetworkMessage){
        sendToAll(server.playerManager.playerList, id, message)
    }

    fun sendToAll(world: ServerWorld, id: Identifier, message: NetworkMessage){
        sendToAll(world.players, id, message)
    }

}