package slabmagic.utils

import net.minecraft.network.codec.PacketCodec
import java.util.function.Function

object PacketCodec2 {
    fun <B, C, T1, T2, T3, T4, T5, T6, T7> tuple(
        codec1: PacketCodec<in B, T1>, from1: Function<C, T1>,
        codec2: PacketCodec<in B, T2>, from2: Function<C, T2>,
        codec3: PacketCodec<in B, T3>, from3: Function<C, T3>,
        codec4: PacketCodec<in B, T4>, from4: Function<C, T4>,
        codec5: PacketCodec<in B, T5>, from5: Function<C, T5>,
        codec6: PacketCodec<in B, T6>, from6: Function<C, T6>,
        codec7: PacketCodec<in B, T7>, from7: Function<C, T7>,
        to: Function7<T1, T2, T3, T4, T5, T6, T7, C>
    ): PacketCodec<B, C> {
        return object : PacketCodec<B, C> {
            override fun decode(`object`: B): C {
                val object2 = codec1.decode(`object`)
                val object3 = codec2.decode(`object`)
                val object4 = codec3.decode(`object`)
                val object5 = codec4.decode(`object`)
                val object6 = codec5.decode(`object`)
                val object7 = codec6.decode(`object`)
                val object8 = codec7.decode(`object`)
                return to.invoke(object2, object3, object4, object5, object6, object7, object8)
            }

            override fun encode(`object`: B, object2: C) {
                codec1.encode(`object`, from1.apply(object2))
                codec2.encode(`object`, from2.apply(object2))
                codec3.encode(`object`, from3.apply(object2))
                codec4.encode(`object`, from4.apply(object2))
                codec5.encode(`object`, from5.apply(object2))
                codec6.encode(`object`, from6.apply(object2))
                codec7.encode(`object`, from7.apply(object2))
            }
        }
    }

    fun <B, C, T1, T2, T3, T4, T5, T6, T7, T8> tuple(
        codec1: PacketCodec<in B, T1>, from1: Function<C, T1>,
        codec2: PacketCodec<in B, T2>, from2: Function<C, T2>,
        codec3: PacketCodec<in B, T3>, from3: Function<C, T3>,
        codec4: PacketCodec<in B, T4>, from4: Function<C, T4>,
        codec5: PacketCodec<in B, T5>, from5: Function<C, T5>,
        codec6: PacketCodec<in B, T6>, from6: Function<C, T6>,
        codec7: PacketCodec<in B, T7>, from7: Function<C, T7>,
        codec8: PacketCodec<in B, T8>, from8: Function<C, T8>,
        to: Function8<T1, T2, T3, T4, T5, T6, T7, T8, C>
    ): PacketCodec<B, C> {
        return object : PacketCodec<B, C> {
            override fun decode(`object`: B): C {
                val object2 = codec1.decode(`object`)
                val object3 = codec2.decode(`object`)
                val object4 = codec3.decode(`object`)
                val object5 = codec4.decode(`object`)
                val object6 = codec5.decode(`object`)
                val object7 = codec6.decode(`object`)
                val object8 = codec7.decode(`object`)
                val object9 = codec8.decode(`object`)

                return to.invoke(object2, object3, object4, object5, object6, object7, object8, object9)
            }

            override fun encode(`object`: B, object2: C) {
                codec1.encode(`object`, from1.apply(object2))
                codec2.encode(`object`, from2.apply(object2))
                codec3.encode(`object`, from3.apply(object2))
                codec4.encode(`object`, from4.apply(object2))
                codec5.encode(`object`, from5.apply(object2))
                codec6.encode(`object`, from6.apply(object2))
                codec7.encode(`object`, from7.apply(object2))
                codec8.encode(`object`, from8.apply(object2))
            }
        }
    }

    fun <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9> tuple(
        codec1: PacketCodec<in B, T1>, from1: Function<C, T1>,
        codec2: PacketCodec<in B, T2>, from2: Function<C, T2>,
        codec3: PacketCodec<in B, T3>, from3: Function<C, T3>,
        codec4: PacketCodec<in B, T4>, from4: Function<C, T4>,
        codec5: PacketCodec<in B, T5>, from5: Function<C, T5>,
        codec6: PacketCodec<in B, T6>, from6: Function<C, T6>,
        codec7: PacketCodec<in B, T7>, from7: Function<C, T7>,
        codec8: PacketCodec<in B, T8>, from8: Function<C, T8>,
        codec9: PacketCodec<in B, T9>, from9: Function<C, T9>,
        to: Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, C>
    ): PacketCodec<B, C> {
        return object : PacketCodec<B, C> {
            override fun decode(`object`: B): C {
                val object2 = codec1.decode(`object`)
                val object3 = codec2.decode(`object`)
                val object4 = codec3.decode(`object`)
                val object5 = codec4.decode(`object`)
                val object6 = codec5.decode(`object`)
                val object7 = codec6.decode(`object`)
                val object8 = codec7.decode(`object`)
                val object9 = codec8.decode(`object`)
                val object10 = codec9.decode(`object`)

                return to.invoke(object2, object3, object4, object5, object6, object7, object8, object9, object10)
            }

            override fun encode(`object`: B, object2: C) {
                codec1.encode(`object`, from1.apply(object2))
                codec2.encode(`object`, from2.apply(object2))
                codec3.encode(`object`, from3.apply(object2))
                codec4.encode(`object`, from4.apply(object2))
                codec5.encode(`object`, from5.apply(object2))
                codec6.encode(`object`, from6.apply(object2))
                codec7.encode(`object`, from7.apply(object2))
                codec8.encode(`object`, from8.apply(object2))
                codec9.encode(`object`, from9.apply(object2))
            }
        }
    }

}