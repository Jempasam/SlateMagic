package slabmagic.datagen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

class SlabMagicDataGenerator: DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        fun side(side: String, action: FabricDataGenerator.()->Unit){
            if(gen.output.toString().contains(side))gen.action()
        }

        side("client"){
            addProvider(::SlabMagicLanguageGenerator)
            addProvider(::SlabMagicModelGenerator)
            addProvider(::SlabMagicTextureGenerator)
        }

        side("main"){
            addProvider(::SlabMagicLootGenerator)
            addProvider(::SlabMagicRecipeGenerator)
            addProvider(::SlabMagicBlockTagGenerator)
        }
    }
}