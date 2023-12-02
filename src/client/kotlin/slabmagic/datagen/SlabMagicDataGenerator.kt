package slabmagic.datagen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import slabmagic.datagen.language.SpellNodeNameGenerator

class SlabMagicDataGenerator: DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        gen.addProvider(::SpellNodeNameGenerator)
    }
}