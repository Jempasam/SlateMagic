package slabmagic.io.deserialization

import slabmagic.io.serialization.EncoderTarget

interface Encoder<in T> {
    fun <Y> encode(target: EncoderTarget<Y>, value: T)
}