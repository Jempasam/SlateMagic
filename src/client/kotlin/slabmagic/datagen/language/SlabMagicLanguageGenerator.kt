package slabmagic.datagen.language

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.util.registry.Registry
import slabmagic.SlabMagicMod
import slabmagic.registry.SlabMagicRegistry
import slabmagic.spell.build.parts.SpellPart

class SlabMagicLanguageGenerator(gen: FabricDataGenerator): FabricLanguageProvider(gen,"en_us") {

    class Builder(private val tb: TranslationBuilder){
        private val set=HashSet<String>()

        fun put(key: String, value: String){
            if(!set.contains(key)){
                tb.add(key,value)
                set.add(key)
            }
        }
    }

    override fun generateTranslations(translationBuilder: TranslationBuilder) {
        Builder(translationBuilder).apply {
            val i18n=SlabMagicMod::i18n


            // Spell Parts
            fun spell_part(spell: String, name: String, desc: String){
                put(i18n("spellnode",spell), name)
                put(i18n("spellnode","$spell.desc"), desc)
            }

            fun spell_part(spell: String, desc: String) = spell_part(spell, idToName(spell), desc)

            spell_part("bomb", "Summon a bomb that cast a spell after a delay")
            spell_part("long_bomb", "Summon a bomb that cast a spell after a long delay")
            spell_part("turret", "Summon a turret that cast a spell many times")

            spell_part("canon", "Shot a projectile that cast a spell on impact")
            spell_part("grenade", "Shot a projectile with curved trajectory")
            spell_part("gun", "Shot a projectile at average range")
            spell_part("punch", "Spawn a small distance projectile")

            spell_part("damage2","Sting","Inflict 2 Damage")
            spell_part("damage4","Hit","Inflict 4 Damage")
            spell_part("damage8","Damage","Inflict 8 Damage")

            spell_part("double", "Repeat a spell 2 times")
            spell_part("many", "Repeat a spell 5 times")
            spell_part("lot_of", "Repeat a spell 10 times")
            spell_part("too_many", "Repeat a spell 40 times")
            spell_part("infinite", "Repeat a spell 200 times")

            spell_part("trap", "Summon a trap that cast a spell on contact")
            spell_part("multi_trap", "Summon a trap that cast a spell on contact 5 times")
            spell_part("large_trap", "Summon a large trap that cast a spell on contact")

            spell_part("push", "Push the target away with a little force")
            spell_part("eject", "Push the target away with a medium force")
            spell_part("propel", "Push the target away with a lot of force")

            spell_part("hand", "Cast a spell 4 block away")
            spell_part("ray", "Cast a spell 10 block away")
            spell_part("long_ray", "Cast a spell 20 block away")
            spell_part("long_distance", "Cast a spell 40 block away")

            spell_part("set_block", "Summon a block")

            spell_part("look_down", "Cast the spell looking lower")
            spell_part("look_up", "Cast the spell looking higher")
            spell_part("vertical_spread", "Cast the spell randomly looking higher and lower")
            spell_part("horizontal_spread", "Cast the spell randomly looking left and right")

            spell_part("zone", "Cast a spell at every entity in a zone")
            spell_part("large_zone", "Cast a spell at every entity in a large zone")
            spell_part("giant_zone", "Cast a spell at every entity in a giant zone")

            spell_part("effect_cloud", "Summon a could of potion effect")
            spell_part("give_effect", "Give an potion effect to the target")

            spell_part("explosion", "Small Explosion", "Summon a small explosion")
            spell_part("explosion2", "Explosion", "Summon an explosion")
            spell_part("explosion4", "Big Explosion", "Summon a big explosion")

            for((key,node) in SlabMagicRegistry.PARTS.entrySet){
                if(node is SpellPart.Spell){
                    val transkey=i18n("spellnode",key.value.path)
                    val text=idToName(key.value.path)
                    put(transkey, text)
                    put("$transkey.desc", text)
                }
            }


            // Spell Parts Type
            fun spell_part_type(id: String, name: String=idToName(id)){
                put(i18n("spellnode_type",id), name)
            }

            spell_part_type("spell")
            spell_part_type("entity")
            spell_part_type("number")
            spell_part_type("block")
            spell_part_type("potion")


            // Inventory Tabs
            fun tab(id: String, name: String=idToName(id)){
                put(i18n("itemGroup",id), name)
            }

            tab("main","Slab Magic")
            tab("spell")
            tab("tools")
            tab("slabs")
            tab("spells")


            // Block
            fun block(id: String, name: String=idToName(id)){
                put(i18n("block",id), name)
            }

            block("slab","Slab of %s")
            block("slab.empty","Slab")
            block("activator_block","Totem Activator Block")
            block("smart_activator_block","Totem Smart Activator Block")
            block("activator_concentrator","Totem Activator Concentrator")
            block("upgraded_activator_concentrator","Upgraded Totem Activator Concentrator")

            for((key,block) in Registry.BLOCK.entrySet){
                if(key.value.namespace.equals(SlabMagicMod.MODID)){
                    block(key.value.path, idToName(key.value.path))
                }
            }


            // Item
            fun item(id: String, name: String=idToName(id)){
                put(i18n("item",id), name)
            }

            item("activator","Totem Activator %s")
            item("activator_wand","Totem Activator Wand")
            item("cost_wand","Totem Cost Wand")
            item("desc_wand","Totem Description Wand")

            item("spell_dust","Dust of %s")
            item("spell_orb","Orb Of %s")
            item("spell_sword","Sword Of %s")

            for((key,item) in Registry.ITEM.entrySet){
                if(key.value.namespace.equals(SlabMagicMod.MODID)){
                    item(key.value.path, idToName(key.value.path))
                }
            }


            // Tooltip
            fun tooltip(id: String, name: String=idToName(id)){
                put(i18n("tooltip",id), name)
            }
            tooltip("empty","Empty")
            tooltip("arguments","Arguments: ")
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