package slabmagic.io.deserialization

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.network.PacketByteBuf

class PacketDecoderTarget(val obj: PacketByteBuf): DecoderTarget<PacketDecoderTarget.Element<*>> {

    override fun asString(value: Element<*>): String = value.asString
    override fun asInt(value: Element<*>) = value.asInt
    override fun asFloat(value: Element<*>) = value.asFloat
    override fun asDouble(value: Element<*>) = value.asDouble
    override fun asBoolean(value: Element<*>) = value.asBoolean
    override fun asSub(value: Element<*>) = value.asSub

    override fun get(str: String): Element<*>{
        return when(val type = obj.readByte().toInt()){
            0 -> StringE(obj.readString())
            1 -> IntE(obj.readInt())
            2 -> FloatE(obj.readFloat())
            3 -> DoubleE(obj.readDouble())
            4 -> BooleanE(obj.readBoolean())
            5 -> SubE(PacketByteBufs.copy(obj.readBytes(obj.readInt())))
            else -> throw Exception("Unknown type $type")
        }
    }
    override fun getOpt(str: String): Element<*>?{
        return if(obj.readBoolean()) get(str) else null
    }

    override fun <T> getMany(str: String, converter: (Element<*>) -> T): List<T> {
        val size = obj.readInt()
        val list = ArrayList<T>(size)
        for(i in 0 until size){
            list.add(converter(get(str)))
        }
        return list
    }

    interface Element<T>{
        fun write(buf: PacketByteBuf)

        val value: T
        val asString: String get() = throw Exception("Not a string")
        val asInt: Int get() = throw Exception("Not an int")
        val asFloat: Float get() = throw Exception("Not a float")
        val asDouble: Double get() = throw Exception("Not a double")
        val asBoolean: Boolean get() = throw Exception("Not a boolean")
        val asSub: PacketDecoderTarget get() = throw Exception("Not a sub")
    }

    class StringE(override val value: String): Element<String>{
        override fun write(buf: PacketByteBuf){ buf.writeString(value) }
        override val asString get() = value
    }

    class IntE(override val value: Int): Element<Int>{
        override fun write(buf: PacketByteBuf){ buf.writeInt(value) }
        override val asInt get() = value
    }

    class FloatE(override val value: Float): Element<Float>{
        override fun write(buf: PacketByteBuf){ buf.writeFloat(value) }
        override val asFloat get() = value
    }

    class DoubleE(override val value: Double): Element<Double>{
        override fun write(buf: PacketByteBuf){ buf.writeDouble(value) }
        override val asDouble get() = value
    }

    class BooleanE(override val value: Boolean): Element<Boolean>{
        override fun write(buf: PacketByteBuf){ buf.writeBoolean(value) }
        override val asBoolean get() = value
    }

    class SubE(override val value: PacketByteBuf): Element<PacketByteBuf>{
        override fun write(buf: PacketByteBuf){ buf.writeBytes(value) }
        override val asSub get() = PacketDecoderTarget(value)
    }
}