package slabmagic.datagen.language

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.data.DataGenerator
import slabmagic.SlabMagicMod
import slabmagic.registry.SlabMagicRegistry
import slabmagic.spell.build.parts.SpellPart

class SpellNodeNameGenerator(val gen: FabricDataGenerator): FabricLanguageProvider(gen,"en_us") {
    override fun generateTranslations(translationBuilder: TranslationBuilder) {
        val realPath=gen.resolveRootDirectoryPath(DataGenerator.OutputType.RESOURCE_PACK).resolve("../../material/assets/${SlabMagicMod.MODID}/lang/en_us.json")
        translationBuilder.add(realPath)

        for((key,node) in SlabMagicRegistry.PARTS.entrySet){
            if(node is SpellPart.Spell){
                val transkey=SlabMagicMod.i18n("spellnode",key.value.path)
                val text=idToName(key.value.path)
                try{ translationBuilder.add(transkey,text) }catch (_: Exception){} // Ignore Duplicate
                try{ translationBuilder.add("$transkey.desc",text) }catch (_: Exception){} // Ignore Duplicate
            }
        }

    }

    fun idToName(id: String): String{
        // Add Spaces in place of _ and between number and word
        var name=id.replace("_"," ").replace(Regex("[^0-9\\s][0-9]")){ it.value[0]+" "+it.value[1] }

        // Add Upper Case
        name=name.replace(Regex(" [a-z]")){ it.value[0]+it.value[1].uppercase() }
        if(name.length>1)name=name[0].uppercase()+name.substring(1)

        return name
    }
}