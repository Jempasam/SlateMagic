package slatemagic.io.serialization

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.network.PacketByteBuf
import slatemagic.io.deserialization.PacketDecoderTarget

class PacketEncoderTarget(val buf: PacketByteBuf): EncoderTarget<PacketDecoderTarget.Element<*>> {
    override fun create(str: String) = PacketDecoderTarget.StringE(str)

    override fun create(int: Int) = PacketDecoderTarget.IntE(int)

    override fun create(float: Float) = PacketDecoderTarget.FloatE(float)

    override fun create(double: Double) = PacketDecoderTarget.DoubleE(double)

    override fun create(boolean: Boolean) = PacketDecoderTarget.BooleanE(boolean)

    override fun createSub() = PacketEncoderTarget(PacketByteBufs.create())



    override fun <T> putMany(key: String, value: List<T>, converter: (T) -> PacketDecoderTarget.Element<*>) {

    }

    override fun putOpt(key: String, value: PacketDecoderTarget.Element<*>?) {
        if(value == null){
            buf.writeBoolean(false)
        }
        else{
            buf.writeBoolean(true)
            value.write(buf)
        }
    }

    override fun put(key: String, value: PacketDecoderTarget.Element<*>) {
        value.write(buf)
    }


    override fun asValue(): PacketDecoderTarget.Element<PacketByteBuf> {
        return PacketDecoderTarget.SubE(buf)
    }
}