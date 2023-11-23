package slatemagic.io.deserialization

import slatemagic.io.serialization.EncoderTarget

interface Encoder<in T> {
    fun <Y> encode(target: EncoderTarget<Y>, value: T)
}