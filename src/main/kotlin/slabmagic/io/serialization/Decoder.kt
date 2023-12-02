package slabmagic.io.serialization

import slabmagic.io.deserialization.DecoderTarget

interface Decoder<out T> {
    fun <Y> decode(target: DecoderTarget<Y>): T
}