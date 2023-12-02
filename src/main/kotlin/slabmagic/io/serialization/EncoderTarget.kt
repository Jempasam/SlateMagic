package slabmagic.io.serialization

interface EncoderTarget<V> {
    fun create(str: String): V
    fun create(int: Int): V
    fun create(float: Float): V
    fun create(double: Double): V
    fun create(boolean: Boolean): V
    fun createSub(): EncoderTarget<V>

    fun put(key: String, value: V)
    fun putOpt(key: String, value: V?)
    fun <T> putMany(key: String, value: List<T>, converter: (T) -> V)
    fun <T> putMany(key: String, value: Iterable<T>, converter: (T) -> V){
        putMany(key, value.toList(), converter)
    }

    fun asValue(): V
}