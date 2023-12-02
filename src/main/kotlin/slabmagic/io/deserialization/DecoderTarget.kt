package slabmagic.io.deserialization

interface DecoderTarget<V> {
    fun asString(value: V): String
    fun asInt(value: V): Int
    fun asFloat(value: V): Float
    fun asDouble(value: V): Double
    fun asBoolean(value: V): Boolean
    fun asSub(value: V): DecoderTarget<V>

    fun get(str: String): V

    fun getOpt(str: String): V?

    fun <T> getMany(str: String, converter: (V) -> T): List<T>
}