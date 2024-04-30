package slabmagic.network

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.packet.CustomPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld

object ServerNetwork {

    fun sendTo(player: ServerPlayerEntity, payload: CustomPayload){
        ServerPlayNetworking.send(player,payload)
    }

    fun sendToAll(players: Iterable<ServerPlayerEntity>, payload: CustomPayload){
        for(player in players) sendTo(player, payload)
    }

    fun sendToAll(server: MinecraftServer, payload: CustomPayload){
        sendToAll(server.playerManager.playerList, payload)
    }

    fun sendToAll(world: ServerWorld, payload: CustomPayload){
        sendToAll(world.players, payload)
    }

}