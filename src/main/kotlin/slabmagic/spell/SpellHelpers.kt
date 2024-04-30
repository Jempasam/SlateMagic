package slabmagic.spell

import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import org.joml.Vector3d
import slabmagic.SlabMagicMod
import kotlin.math.max
import kotlin.math.min

fun Byte.converge(value: Byte, strength: Float): Byte {
    val istrength=strength.toInt()
    if(value>this)return min(value.toInt(), this+istrength).toByte()
    else if(value<this)return max(value.toInt(), this-istrength).toByte()
    return this
}

fun pierce(context: SpellContext){
    if(!context.world.isAir(BlockPos.ofFloored(context.pos)))return

    val actual= Vector3d(context.pos.x, context.pos.y, context.pos.z)
    val directionVector= Vec3d.fromPolar(context.direction.x,context.direction.y).multiply(0.05)
    for(i in 0..<15){
        val blockpos= BlockPos.ofFloored(actual.x,actual.y,actual.z)
        if(!context.world.isAir(blockpos)){
            break
        }
        actual.x+=directionVector.x
        actual.y+=directionVector.y
        actual.z+=directionVector.z
    }
    context.pos= Vec3d(actual.x, actual.y, actual.z)
}

fun MutableText.times(count: Int)=
    if(count==1)this.append(" ")
    else this.append(translatable("spell_common","times",count))

fun spellDesc(id: String, vararg objs: Any) = translatable("spell", "$id.desc", *objs)
fun <T> spellDescIt(id: String, ite: Iterable<T>, vararg objs: Any, opt: (T)->Array<Text>): MutableText{
    val ret=Text.empty()
    val it=ite.iterator()
    var v=it.next()
    while(it.hasNext()){
        ret.append(spellDesc("$id.element", *opt(v)))
        v=it.next()
    }
    ret.append(spellDesc("$id.element.last", *opt(v)))
    return spellDesc(id, ret, *objs)
}
fun spellName(id: String, vararg objs: Any) = translatable("spell", "$id.name", *objs)

private fun translatable(type: String, id: String, vararg objs: Any)
    = Text.translatable(SlabMagicMod.i18n(type,id), *objs)