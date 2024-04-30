package slabmagic.datagen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

class SlabMagicDataGenerator: DataGeneratorEntrypoint {

    private inline fun target(target: String, action: ()->Unit){
        if(System.getProperty("datagenTarget")==target){
            action()
        }
    }

    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        gen.createPack().apply {
            target("client"){
                addProvider(::SlabMagicLanguageGenerator)
                addProvider(::SlabMagicModelGenerator)
                //addProvider(::SlabMagicTextureGenerator)
            }
            target("main"){
                addProvider(::SlabMagicLootGenerator)
                addProvider(::SlabMagicRecipeGenerator)
                addProvider(::SlabMagicBlockTagGenerator)
            }
        }
    }
}