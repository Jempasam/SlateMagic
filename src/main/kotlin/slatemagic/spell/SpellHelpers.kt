package slatemagic.spell

import kotlin.math.max
import kotlin.math.min

fun Byte.converge(value: Byte, strength: Float): Byte {
    val istrength=strength.toInt()
    if(value>this)return min(value.toInt(), this+istrength).toByte()
    else if(value<this)return max(value.toInt(), this-istrength).toByte()
    return this
}