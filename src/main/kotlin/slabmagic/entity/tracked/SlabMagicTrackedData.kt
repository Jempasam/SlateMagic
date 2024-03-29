package slabmagic.entity.tracked

import net.minecraft.entity.data.TrackedDataHandler
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import slabmagic.entity.data.SpellEntity
import slabmagic.shape.SpellShape

object SlabMagicTrackedData {

    val SPELL_SHAPE = register( {buf,shape -> buf.writeString(shape.toCode())}, {buf -> SpellShape(buf.readString())} )

    val SPELL_DATA = register( {buf,data -> data.write(buf)}, {buf -> SpellEntity.Data().apply {read(buf)} })

    val BLOCK = register(
        { buf, block -> buf.writeString(Registry.BLOCK.getId(block).toString()) },
        { buf -> Registry.BLOCK.get(Identifier(buf.readString())) }
    )

    fun <T> register(writer: PacketByteBuf.PacketWriter<T>, reader: PacketByteBuf.PacketReader<T>): TrackedDataHandler<T> {
        val handler=TrackedDataHandler.of(writer, reader)
        TrackedDataHandlerRegistry.register(handler)
        return handler
    }
}