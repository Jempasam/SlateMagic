package slatemagic.io.serialization

import slatemagic.io.deserialization.DecoderTarget

interface Decoder<out T> {
    fun <Y> decode(target: DecoderTarget<Y>): T
}