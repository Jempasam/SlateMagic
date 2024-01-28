package slabmagic.io

import net.minecraft.block.Block
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import slabmagic.io.deserialization.DecoderTarget
import slabmagic.io.serialization.EncoderTarget

// Registry
fun <Y,T> EncoderTarget<T>.createReg(obj: Y, reg: Registry<Y>): T{
    val id= reg.getId(obj) ?: throw Exception(obj.toString()+" is not registred")
    return create(id.toString())
}

fun <Y,T> DecoderTarget<T>.asReg(element: T, reg: Registry<Y>): Y{
    val key=RegistryKey.of(reg.key, Identifier(asString(element)))
    return reg.getOrThrow(key)
}

// Block
fun <T> EncoderTarget<T>.create(block: Block): T = createReg(block,Registry.BLOCK)
fun <T> DecoderTarget<T>.asBlock(element: T) = asReg(element, Registry.BLOCK)

// Potion
fun <T> EncoderTarget<T>.create(effect: StatusEffect): T = createReg(effect,Registry.STATUS_EFFECT)
fun <T> DecoderTarget<T>.asStatusEffect(element: T) = asReg(element, Registry.STATUS_EFFECT)

// Vec3d
fun <T> EncoderTarget<T>.create(vec: Vec3d): T{
    val ret=createSub()
    ret.put("x",ret.create(vec.x))
    ret.put("y",ret.create(vec.y))
    ret.put("z",ret.create(vec.z))
    return ret.asValue()
}

fun <T> DecoderTarget<T>.asVec3d(element: T): Vec3d{
    val sub=asSub(element)
    return Vec3d(
        asDouble(sub.get("x")),
        asDouble(sub.get("y")),
        asDouble(sub.get("z"))
    )
}

// Text
fun <T> EncoderTarget<T>.create(txt: Text): T{
    return create(txt.string)
}

fun <T> DecoderTarget<T>.asText(element: T): Text{
    return Text.of(asString(element))
}