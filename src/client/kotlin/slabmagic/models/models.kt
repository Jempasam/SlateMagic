package slabmagic.models

import net.minecraft.block.Block
import net.minecraft.data.client.*
import net.minecraft.registry.Registries
import net.minecraft.state.property.Property
import net.minecraft.util.Identifier
import java.util.*

// MINECRAFT NAMESPACE
/** Default minecraft namespace */
object Minecraft{
    operator fun div(path: String) = Identifier("minecraft",path)
}

class IdentifierBuilder(val block: Block){
    val id=Registries.BLOCK.getId(block)
    val namespace=id.namespace
    val path=id.path
    val splitted= path.split("_")

    fun apply(pattern: String): Identifier{
        var pattern=pattern.replace("%n",path)
        for(i in splitted.indices) pattern=pattern.replace("%${i+1}",splitted[i])
        return Identifier(namespace,pattern)
    }
}


// MODELS
/** Create a model with a given parent */
fun model(parent: Identifier, vararg keys: TextureKey) = Model(Optional.of(parent), Optional.empty(), *keys)
/** Create a model with no parent */
fun model(vararg keys: TextureKey) = Model(Optional.empty(), Optional.empty(), *keys)


// TEXTURE KEYS
/** Create a texture key from a string */
fun key(id: String) = TextureKey.of(id)


// TEXTURE MAPS
/** Create a static texture map */
fun staticTextures(vararg textures: Pair<TextureKey,Identifier>): (Block)->TextureMap{
    val map= TextureMap()
    textures.forEach { (key,texture) -> map.put(key,texture) }
    return {map}
}
/**
 * Create a dynamic texture map, with same namespace for the texture and the block,
 * and a path with patterns.
 * */
fun textures(vararg textures: Pair<TextureKey,String>) = { block: Block ->
    val map= TextureMap()
    val builder= IdentifierBuilder(block)
    textures.forEach { (key,name) -> map.put(key,builder.apply(name)) }
    map
}


// TEXTURED MODELS
/** Associate a model with a texture map */
infix fun Model.with(tmap: (Block)->TextureMap)
    = TexturedModel.makeFactory(tmap, this)


// BLOCK STATE VARIANTS
val X = VariantSettings.X
val Y = VariantSettings.Y
val UVLOCK = VariantSettings.UVLOCK
val WEIGHT = VariantSettings.WEIGHT
fun<T> variant(pattern: String, vararg variants: Pair<VariantSetting<T>,T>) = { block: Block ->
    val variant=BlockStateVariant.create()
    val builder= IdentifierBuilder(block)

    variant.put(VariantSettings.MODEL, builder.apply(pattern))
    variants.forEach { (setting,value) -> variant.put(setting,value) }
    variant
}

fun variant(pattern: String) = variant<Any>(pattern)

// BLOCK STATE VARIANT MAP
fun <A: Comparable<A>>
        blockState(p1: Property<A>, vararg states: Pair<A,(Block)->BlockStateVariant>) = { block: Block ->
    val variant=BlockStateVariantMap.create(p1)
    states.forEach { (value,state) -> variant.register(value,state(block)) }
    variant
}

fun <A: Comparable<A>, B: Comparable<B>>
        blockState(p1: Property<A>, p2: Property<B>, vararg states: Pair<Pair<A,B>,(Block)->BlockStateVariant>) = { block: Block ->
    val variant=BlockStateVariantMap.create(p1,p2)
    states.forEach { (value,state) -> variant.register(value.first,value.second,state(block)) }
    variant
}

fun <A: Comparable<A>, B: Comparable<B>, C: Comparable<C>>
        blockState(p1: Property<A>, p2: Property<B>, p3: Property<C>, vararg states: Pair<Triple<A,B,C>,(Block)->BlockStateVariant>) = { block: Block ->
    val variant=BlockStateVariantMap.create(p1,p2,p3)
    states.forEach { (value,state) -> variant.register(value.first,value.second,value.third,state(block)) }
    variant
}

fun <A: Comparable<A>, B: Comparable<B>, C: Comparable<C>, D: Comparable<D>>
        blockState(p1: Property<A>, p2: Property<B>, p3: Property<C>, p4: Property<D>, vararg states: Pair<Quaduble<A,B,C,D>,(Block)->BlockStateVariant>) = { block: Block ->
    val variant=BlockStateVariantMap.create(p1,p2,p3,p4)
    states.forEach { (value,state) -> variant.register(value.first,value.second,value.third,value.fourth,state(block)) }
    variant
}

class Quaduble<A,B,C,D>(val first: A, val second: B, val third: C, val fourth: D){
    override fun equals(other: Any?): Boolean {
        if(other !is Quaduble<*,*,*,*>) return false
        return first==other.first && second==other.second && third==other.third && fourth==other.fourth
    }
    override fun hashCode(): Int {
        return Objects.hash(first,second,third,fourth)
    }
}


// UPLOAD
fun BlockStateModelGenerator.upload(block: Block, model: TexturedModel.Factory, suffix: String=""): Identifier{
    return model.upload(block, suffix, this.modelCollector)
}

fun BlockStateModelGenerator.upload(block: Block, variants: (Block)->BlockStateVariantMap){
    this.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants(block)))
}