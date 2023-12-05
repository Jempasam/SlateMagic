package slabmagic.datagen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import slabmagic.datagen.language.SlabMagicLanguageGenerator
import slabmagic.datagen.language.SlabMagicLootGenerator
import slabmagic.datagen.language.SlabMagicModelGenerator

class SlabMagicDataGenerator: DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        gen.addProvider(::SlabMagicLanguageGenerator)
        gen.addProvider(::SlabMagicLootGenerator)
        gen.addProvider(::SlabMagicModelGenerator)
        gen.addProvider(::SlabMagicModelGenerator)
    }
}