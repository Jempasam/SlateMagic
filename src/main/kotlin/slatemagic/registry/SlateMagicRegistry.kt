package slatemagic.registry

import com.mojang.serialization.Lifecycle
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.MutableRegistry
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import net.minecraft.util.registry.SimpleRegistry
import slatemagic.SlateMagicMod
import slatemagic.spell.build.SpellNode
import slatemagic.spell.build.SpellNodeDecoder
import slatemagic.spell.effect.SpellEffect
import slatemagic.spell.effect.action.*
import slatemagic.spell.effect.move.*

object SlateMagicRegistry {

    val DYNAMICS= registry<Registry<*>>(key("registries"))

    var EFFECTS = dynamicRegistry<SpellEffect>(key("effects"))

    var NODES = dynamicRegistry<SpellNode<*>>(key("nodes"))

    val NODE_DECODERS = registry<SpellNodeDecoder<*>>(key("node_decoders"))

    fun clearEffects() = dynamicRegistry<SpellEffect>(key("effects"))

    fun clearNodes() = dynamicRegistry<SpellNode<*>>(key("effects"))

    init{
        fun spell(id: String, spell: SpellEffect){
            Registry.register(EFFECTS, SlateMagicMod.id(id), spell)
        }
        spell("explosion", ExplosionSpellEffect())
        spell("grenade", ProjectileSpellEffect(0.4f,100,ExplosionSpellEffect()))
        spell("bounce_grenade", ProjectileSpellEffect(0.4f,100, ProjectileSpellEffect(0.4f,100,ExplosionSpellEffect())))
        spell("canon", RaytraceSpellEffect(10, ExplosionSpellEffect()))
        spell("machine_gun", TurretSpellEffect(20,10, RaytraceSpellEffect(10, ExplosionSpellEffect())))
        spell("mine", TrapSpellEffect(5f,10, ExplosionSpellEffect()))
        spell("auto_gun", TrapSpellEffect(10f,10, RaytraceSpellEffect(5, ExplosionSpellEffect())))
        spell("bomb", TurretSpellEffect(100,1, ExplosionSpellEffect()))

        spell("creeper", RaytraceSpellEffect(10, EntitySpellEffect(EntityType.CREEPER)))
        spell("spider", RaytraceSpellEffect(10, EntitySpellEffect(EntityType.SPIDER)))
        spell("cave_spider", RaytraceSpellEffect(10, EntitySpellEffect(EntityType.CAVE_SPIDER)))
        spell("chicken", RaytraceSpellEffect(10, EntitySpellEffect(EntityType.CHICKEN)))

        spell("speed_cloud", PotionCloudSpellEffect(StatusEffects.SPEED))
        spell("regeneration_cloud", PotionCloudSpellEffect(StatusEffects.REGENERATION))

        spell("poison_cloud", PotionCloudSpellEffect(StatusEffects.POISON))
        spell("poison_grenade", ProjectileSpellEffect(0.4f,100,
            ZoneSpellEffect(Vec3d(4.0, 4.0,4.0), PotionSpellEffect(StatusEffects.POISON))
        )
        )
        spell("poison_bomb", TurretSpellEffect(100,1, ZoneSpellEffect(Vec3d(8.0, 3.0,8.0), PotionSpellEffect(StatusEffects.POISON))))
        spell("poison_trap", TrapSpellEffect(2f,1, ZoneSpellEffect(Vec3d(8.0, 3.0,8.0), PotionSpellEffect(StatusEffects.POISON))))

        spell("levitation_zone", ZoneSpellEffect(Vec3d(8.0, 3.0,8.0), PotionSpellEffect(StatusEffects.LEVITATION)))
        spell("self_speed", PotionSpellEffect(StatusEffects.SPEED))

        spell("fire", BlockSpellEffect(Blocks.FIRE))
        spell("ice", BlockSpellEffect(Blocks.ICE))
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