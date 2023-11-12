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
import slatemagic.spell.Spell
import slatemagic.spell.action.*
import slatemagic.spell.move.RaytraceSpell
import slatemagic.spell.move.TrapSpell
import slatemagic.spell.move.TurretSpell
import slatemagic.spell.move.ZoneSpell

object SlateMagicRegistry {

    val DYNAMICS= registry<Registry<*>>(key("registries"))

    val SPELLS = dynamicRegistry<Spell>(key("spells"))

    init{
        fun spell(id: String, spell: Spell){
            Registry.register(SPELLS, SlateMagicMod.id(id), spell)
        }
        spell("explosion", ExplosionSpell())
        spell("canon", RaytraceSpell(10, ExplosionSpell()))
        spell("machine_gun", TurretSpell(20,10, RaytraceSpell(10, ExplosionSpell())))
        spell("mine", TrapSpell(5f,10, ExplosionSpell()))
        spell("auto_gun", TrapSpell(10f,10, RaytraceSpell(5, ExplosionSpell())))
        spell("bomb", TurretSpell(100,1, ExplosionSpell()))

        spell("creeper", RaytraceSpell(10, EntitySpell(EntityType.CREEPER)))
        spell("spider", RaytraceSpell(10, EntitySpell(EntityType.SPIDER)))
        spell("cave_spider", RaytraceSpell(10, EntitySpell(EntityType.CAVE_SPIDER)))
        spell("chicken", RaytraceSpell(10, EntitySpell(EntityType.CHICKEN)))

        spell("speed_cloud", PotionCloudSpell(StatusEffects.SPEED))
        spell("regeneration_cloud", PotionCloudSpell(StatusEffects.REGENERATION))

        spell("poison_cloud", PotionCloudSpell(StatusEffects.POISON))
        spell("poison_bomb", TurretSpell(100,1, ZoneSpell(Vec3d(8.0, 3.0,8.0), PotionSpell(StatusEffects.POISON))))
        spell("poison_trap", TrapSpell(2f,1, ZoneSpell(Vec3d(8.0, 3.0,8.0), PotionSpell(StatusEffects.POISON))))

        spell("levitation_zone", ZoneSpell(Vec3d(8.0, 3.0,8.0), PotionSpell(StatusEffects.LEVITATION)))
        spell("self_speed", PotionSpell(StatusEffects.SPEED))

        spell("fire", BlockSpell(Blocks.FIRE))
        spell("ice", BlockSpell(Blocks.ICE))
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