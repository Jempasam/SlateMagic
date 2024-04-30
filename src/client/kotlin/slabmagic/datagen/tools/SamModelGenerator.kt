package slabmagic.datagen.tools

import net.minecraft.block.Block
import net.minecraft.data.client.*
import net.minecraft.registry.Registries
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.IntProperty
import net.minecraft.state.property.Property
import net.minecraft.util.Identifier
import java.util.*

class SamModelGenerator(val generator: BlockStateModelGenerator) {

    fun texid(block: Block, suffix: String="") = Registries.BLOCK.getId(block).let { Identifier(it.namespace,"block/"+it.path+suffix) }

    /* MODELS */
    fun mChildOf(parent: Identifier, vararg tkeys: TextureKey) = Model(Optional.of(parent), Optional.empty(), *tkeys)

    /* TEXTURED MODEL */
    fun tmPillar(side: Block, top: Block) = TexturedModel.makeFactory(
        {TextureMap().put(TextureKey.SIDE,texid(side)).put(TextureKey.END,texid(top))},
        Models.CUBE_COLUMN
    )

    /* TEXTURES */
    fun tAll(model: Model, texture: Identifier)
        = TexturedModel.makeFactory({ block -> TextureMap.all(texture) }, model)

    fun t(model: Model, vararg tkeys: Pair<TextureKey,Identifier>): TexturedModel.Factory{
        val map=TextureMap()
        tkeys.forEach { (key,texture) -> map.put(key,texture) }
        return TexturedModel.makeFactory({ block -> map }, model)
    }

    fun registerOnBool(block: Block, prop: BooleanProperty, a: TexturedModel.Factory, b: TexturedModel.Factory){
        val m1=a.upload(block, "_"+prop.name, generator.modelCollector)
        val m2=b.upload(block, generator.modelCollector)
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block).coordinate(BlockStateModelGenerator.createBooleanModelMap(prop,m1,m2))
        )
    }

    fun registerOnInt(block: Block, prop: IntProperty, model: TexturedModel.Factory){
        val ids = prop.values.map{ model.upload(block, "_$it", generator.modelCollector) }
        val variants=BlockStateVariantMap.create(prop)
        prop.values.zip(ids).forEach { (value,id) ->
            variants.register(value,BlockStateVariant.create().put(VariantSettings.MODEL,id))
        }
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants))
    }

    fun <T: Comparable<T>> registerOnProp(block: Block, prop: Property<T>, vararg models: Pair<T,TexturedModel.Factory>){
        var first=true
        val models_ids=models.map{ (value,model) ->
            val id=model.upload(block, if(!first) "_$value" else "", generator.modelCollector)
            first=false
            value to id
        }
        val variants=BlockStateVariantMap.create(prop)
        models_ids.forEach { (value,id) ->
            variants.register(value,BlockStateVariant.create().put(VariantSettings.MODEL,id))
        }
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants))
    }

    fun <T: Comparable<T>, Y: Comparable<Y>> registerOnProp(block: Block, prop: Property<T>, prop2: Property<Y>, vararg models: Pair<Pair<T,Y>,TexturedModel.Factory>){
        var first=true
        val models_ids=models.map{ (value,model) ->
            val id=model.upload(block, if(!first) "_$value" else "", generator.modelCollector)
            first=false
            value to id
        }
        val variants=BlockStateVariantMap.create(prop,prop2)
        models_ids.forEach { (pair,id) ->
            variants.register(pair.first,pair.second,BlockStateVariant.create().put(VariantSettings.MODEL,id))
        }
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants))
    }

    fun <T: Comparable<T>> registerOnProp(block: Block, prop: Property<T>, models: List<Pair<T,TexturedModel.Factory>>)
        = registerOnProp(block,prop,*models.toTypedArray())

    fun registerPillar(block: Block, topBlock: Block){
        generator.registerSingleton(block,tmPillar(block,topBlock))
    }
}