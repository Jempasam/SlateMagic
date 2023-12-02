package slabmagic.network

import net.minecraft.network.PacketByteBuf

interface NetworkMessage {

    fun write(): PacketByteBuf

}