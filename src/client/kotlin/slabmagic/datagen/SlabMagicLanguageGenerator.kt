package slabmagic.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.util.registry.Registry
import slabmagic.SlabMagicMod
import slabmagic.registry.SlabMagicRegistry
import slabmagic.spell.build.parts.SpellParts
import kotlin.reflect.full.hasAnnotation

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

            spell_part("push", "Push the target away with a little force")
            spell_part("eject", "Push the target away with a medium force")
            spell_part("propel", "Push the target away with a lot of force")

            spell_part("set_block", "Summon a block")

            spell_part("effect_cloud", "Summon a could of potion effect")
            spell_part("give_effect", "Give an potion effect to the target")

            spell_part("explosion", "Small Explosion", "Summon a small explosion")
            spell_part("explosion2", "Explosion", "Summon an explosion")
            spell_part("explosion4", "Big Explosion", "Summon a big explosion")

            spell_part("low_shield", "Summon a shield that cast a spell on an attacker")
            spell_part("shield", "Summon a shield that cast a spell on an attacker 3 times")
            spell_part("attack", "Summon a magic orb on an entity that cast a spell on hit")
            spell_part("enchanting", "Summon a magic orb on an entity that cast a spell on hit 3 times")

            spell_part("dust", "Summon 5 dust item that cast a spell on use")
            spell_part("orb", "Summon an orb item that cast a spell on use")
            spell_part("sword", "Summon a sword item that cast a spell on hit")

            spell_part("fork", "Cast two different spells at the same time")


            for((key,node) in SlabMagicRegistry.PARTS.entrySet){
                val transkey=i18n("spellnode",key.value.path)
                val text=idToName(key.value.path)
                if(node::class.hasAnnotation<SpellParts.NameI8n>()){
                    put(transkey, text)
                }
                if(node::class.hasAnnotation<SpellParts.DescI8n>()){
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
            tab("old_slabs")
            tab("spells")
            tab("buildings")


            // Block
            fun block(id: String, name: String=idToName(id)){
                put(i18n("block",id), name)
            }

            block("slab","Slab of %s")
            block("slab.empty","Slab")
            block("old_slab","Old Slab of %s")
            block("old_slab.empty","Old Slab")
            block("energy_slab","Energy Slab of %s")
            block("energy_slab.empty","Energy Slab")
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
            item("spell_wand","Wand Of %s")

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


            // Spell
            fun spell_common(id: String, desc: String) = put(i18n("spell_common",id), name)
            fun spell(id: String, title: String, desc: String) {
                put(i18n("spell","$id.name"), title)
                put(i18n("spell","$id.desc"), desc)
            }
            fun spell(id: String, title: String, desc: String, elements: String, last: String) {
                put(i18n("spell","$id.name"), title)
                put(i18n("spell","$id.desc"), desc)
            }

            spell_common("times",", %d times, ")

            spell("stub", "Stub", "do nothing")
            spell("push", "Push", "push with a strength of %d")
            spell("give_potion", "Give %s", "give %fs of %s %d")
            spell("potion_cloud", "Cloud of %s", "summon a cloud of %s")
            spell("use_item_on_block", "%s", "simulate using %s on a block")
            spell("use_item_on_entity", "%s", "simulate using %s on an entity")
            spell("use_item_in_direction", "%s", "simulate using %s in a direction")
            spell("explosion", "Explosion", "summon a explosion of power %s")
            spell("fire_explosion", "Fire Explosion", "summon a fiery explosion of power %s")
            spell("summon_entity", "Summon %s", "summon a %s")
            spell("damage", "Damage", "inflict %d damages")

            spell("break", "Break", "break with a strength of %f")
            spell("block", "%s", "summon a %s block")
            spell("set_block", "Set %s", "summon a  %s")
            spell("block_replace", "%s Transmutation", "replace %s with %s", "%s and", "%s")
            spell("block_interaction", "%s Contact", "simulate constact with %s")

            spell("at_eye", "Eye-%s", ", at eye, %s")
            spell("fill", "Square of %s", "on each block of an area of %dx%dx%d, %s")

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