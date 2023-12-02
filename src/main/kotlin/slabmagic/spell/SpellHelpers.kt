package slabmagic.spell

import net.minecraft.client.util.math.Vector3d
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import kotlin.math.max
import kotlin.math.min

fun Byte.converge(value: Byte, strength: Float): Byte {
    val istrength=strength.toInt()
    if(value>this)return min(value.toInt(), this+istrength).toByte()
    else if(value<this)return max(value.toInt(), this-istrength).toByte()
    return this
}

fun pierce(context: SpellContext){
    if(!context.world.isAir(BlockPos(context.pos)))return

    val actual= Vector3d(context.pos.x, context.pos.y, context.pos.z)
    val directionVector= Vec3d.fromPolar(context.direction.x,context.direction.y).multiply(0.05)
    for(i in 0..<20){
        val blockpos= BlockPos(actual.x,actual.y,actual.z)
        if(!context.world.isAir(blockpos)){
            break
        }
        actual.x+=directionVector.x
        actual.y+=directionVector.y
        actual.z+=directionVector.z
    }
    context.pos= Vec3d(actual.x, actual.y, actual.z)
}