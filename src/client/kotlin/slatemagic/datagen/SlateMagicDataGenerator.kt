package slatemagic.datagen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import slatemagic.datagen.language.SpellNodeNameGenerator

class SlateMagicDataGenerator: DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        gen.addProvider(::SpellNodeNameGenerator)
    }
}