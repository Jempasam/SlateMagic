package slabmagic.io

import slabmagic.io.deserialization.Encoder
import slabmagic.io.serialization.Decoder

interface Codec<T>: Encoder<T>, Decoder<T>