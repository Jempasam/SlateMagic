package slabmagic.registry

import com.mojang.serialization.Lifecycle
import net.minecraft.registry.*
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryEntryInfo
import slabmagic.SlabMagicMod
import slabmagic.spell.build.AssembledSpell
import slabmagic.spell.build.assembleSpell
import slabmagic.spell.build.parts.SlabMagicSpellParts
import slabmagic.spell.build.parts.SpellPart
import java.util.*

object SlabMagicRegistry {

    val DYNAMICS= registry<Registry<*>>(key("registries"))

    var EFFECTS = dynamicRegistry<AssembledSpell>(key("effects"))

    var PARTS = dynamicRegistry<SpellPart<*>>(key("nodes"))



    init{
        fun spell(id: String, vararg parts: RegistryEntry<SpellPart<*>>){
            Registry.register(EFFECTS, SlabMagicMod.id(id), listOf(*parts).map{it.value()}.assembleSpell())
        }

        SlabMagicSpellParts.apply {
            spell("explosion", EXPLOSION2)
            spell("grenade", GRENADE, EXPLOSION2)
            spell("bounce_grenade", GRENADE, LOOK_UP, GRENADE, EXPLOSION2)
            spell("canon", RAY, EXPLOSION2)
            spell("machine_gun", TURRET, RAY, EXPLOSION)
            spell("mine", TRAP, EXPLOSION2)
            spell("auto_gun", TRAP, RAY, EXPLOSION)
            spell("bomb", BOMB, EXPLOSION2)

            spell("creeper", RAY, CREEPER)
            spell("spider", RAY, SPIDER)
            spell("cave_spider", RAY, CAVE_SPIDER)

            spell("speed_cloud", EFFECT_CLOUD, POTION_SPEED)
            spell("regeneration_cloud", EFFECT_CLOUD, POTION_REGENERATION)

            spell("poison_cloud", EFFECT_CLOUD, POTION_POISON)
            spell("poison_grenade", GRENADE, LARGE_ZONE, GIVE_EFFECT, POTION_POISON)
            spell("poison_bomb", BOMB, LARGE_ZONE, GIVE_EFFECT, POTION_POISON)
            spell("poison_trap", TRAP, ZONE, GIVE_EFFECT, POTION_POISON)

            spell("levitation_zone", LARGE_ZONE, GIVE_EFFECT, POTION_LEVITATION)
            spell("self_speed", GIVE_EFFECT, POTION_SPEED)

            spell("fire", SET_BLOCK, BLOCK_FIRE)
            spell("ice", SET_BLOCK, BLOCK_ICE)

        }
    }

    /* STATICS */
    private fun <T>key(id: String): RegistryKey<Registry<T>>{
        return RegistryKey.ofRegistry(SlabMagicMod.id(id))
    }

    private fun <T> registry(key: RegistryKey<Registry<T>>): SimpleRegistry<T>{
        val reg=SimpleRegistry(key, Lifecycle.stable())
        (Registries.REGISTRIES as MutableRegistry<Registry<T>>).add(key, reg, RegistryEntryInfo(Optional.empty(),Lifecycle.stable()))
        return reg
    }

    private fun <T> dynamicRegistry(key: RegistryKey<Registry<T>>): SimpleRegistry<T>{
        val reg=SimpleRegistry(key, Lifecycle.stable())
        Registry.register(DYNAMICS, key.value, reg)
        return reg
    }
}