package slabmagic.datagen.language

import net.minecraft.block.Block
import net.minecraft.data.client.*
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.*

class SamModelGenerator(val generator: BlockStateModelGenerator) {

    fun textureId(block: Block, suffix: String="") = Registry.BLOCK.getId(block).let { Identifier(it.namespace,"block/"+it.path+suffix) }

    fun modelChildOf(parent: Identifier, vararg tkeys: TextureKey) = Model(Optional.of(parent), Optional.empty(), *tkeys)

    fun textureAll(model: Model, texture: Identifier)
        = TexturedModel.makeFactory({ block -> TextureMap.all(texture) }, model)

    fun registerOnBool(block: Block, prop: BooleanProperty, a: TexturedModel.Factory, b: TexturedModel.Factory){
        val m1=a.upload(block, generator.modelCollector)
        val m2=b.upload(block, "_"+prop.name, generator.modelCollector)
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block).coordinate(BlockStateModelGenerator.createBooleanModelMap(prop,m1,m2))
        )
    }
}