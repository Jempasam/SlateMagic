package slatemagic.io

import slatemagic.io.deserialization.Encoder
import slatemagic.io.serialization.Decoder

interface Codec<T>: Encoder<T>, Decoder<T> {

}