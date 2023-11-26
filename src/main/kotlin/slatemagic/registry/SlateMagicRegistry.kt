package slatemagic.registry

import com.mojang.serialization.Lifecycle
import net.minecraft.util.registry.MutableRegistry
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import net.minecraft.util.registry.SimpleRegistry
import slatemagic.SlateMagicMod
import slatemagic.spell.build.SlateMagicSpellNodes
import slatemagic.spell.build.SpellNode
import slatemagic.spell.build.SpellNodeDecoder
import slatemagic.spell.build.assembleSpell
import slatemagic.spell.effect.SpellEffect

object SlateMagicRegistry {

    val DYNAMICS= registry<Registry<*>>(key("registries"))

    var EFFECTS = dynamicRegistry<SpellEffect>(key("effects"))

    var NODES = dynamicRegistry<SpellNode<*>>(key("nodes"))

    val NODE_DECODERS = registry<SpellNodeDecoder<*>>(key("node_decoders"))

    fun clearEffects() = dynamicRegistry<SpellEffect>(key("effects"))

    fun clearNodes() = dynamicRegistry<SpellNode<*>>(key("effects"))

    init{
        fun spell(id: String, vararg parts: SpellNode<*>){
            Registry.register(EFFECTS, SlateMagicMod.id(id), listOf(*parts).assembleSpell().effect)
        }

        SlateMagicSpellNodes.apply {
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
        return RegistryKey.ofRegistry(SlateMagicMod.id(id));
    }

    private fun <T> registry(key: RegistryKey<Registry<T>>): SimpleRegistry<T>{
        val reg=SimpleRegistry(key, Lifecycle.stable(), null)
        (Registry.REGISTRIES as MutableRegistry<in Any>).add(key, reg, Lifecycle.stable())
        return reg
    }

    private fun <T> dynamicRegistry(key: RegistryKey<Registry<T>>): SimpleRegistry<T>{
        val reg=SimpleRegistry(key, Lifecycle.stable(), null)
        Registry.register(DYNAMICS, key.value, reg)
        return reg
    }
}