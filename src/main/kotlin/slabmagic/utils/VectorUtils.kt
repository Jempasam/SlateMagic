package slabmagic.utils

import org.joml.Vector3f

fun Vector3f.coerceIn(min: Float, max: Float): Vector3f {
    x = x.coerceIn(min, max)
    y = y.coerceIn(min, max)
    z = z.coerceIn(min, max)
    return this
}

inline fun Vector3f.modify(fn: (Float) -> Float): Vector3f {
    x = fn(x)
    y = fn(y)
    z = fn(z)
    return this
}