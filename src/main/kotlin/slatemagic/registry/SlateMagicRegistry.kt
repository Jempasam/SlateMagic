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
import slatemagic.spell.build.node.action.DamageSpellNode
import slatemagic.spell.build.node.action.ExplosionSpellNode
import slatemagic.spell.build.node.action.PotionSpellNode
import slatemagic.spell.build.node.move.ProjectileSpellNode
import slatemagic.spell.build.node.move.TrapSpellNode
import slatemagic.spell.build.node.move.TurretSpellNode
import slatemagic.spell.build.node.move.ZoneSpellNode
import slatemagic.spell.effect.SpellEffect
import slatemagic.spell.effect.action.*
import slatemagic.spell.effect.move.*

object SlateMagicRegistry {

    val DYNAMICS= registry<Registry<*>>(key("registries"))

    val EFFECTS = dynamicRegistry<SpellEffect>(key("effects"))

    val SPELL_NODES = dynamicRegistry<SpellNode<*>>(key("nodes"))

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

        fun <N: SpellNode<*>> node(id: String, node: N){
            Registry.register(SPELL_NODES, SlateMagicMod.id(id), node)
        }

        node("damage2", DamageSpellNode(2,"Damage 2"))
        node("damage4", DamageSpellNode(4,"Damage 4"))
        node("damage8", DamageSpellNode(8,"Damage 8"))

        node("grenade", ProjectileSpellNode(0.4f,60, "Grenade"))
        node("canon", ProjectileSpellNode(0.8f,50, "Canon"))
        node("gun", ProjectileSpellNode(1.5f,20, "Gun"))
        node("punch", ProjectileSpellNode(1.0f,5, "Punch"))

        node("explosion", ExplosionSpellNode(1f,"Small Explosion"))
        node("explosion2", ExplosionSpellNode(1.5f,"Explosion"))
        node("explosion3", ExplosionSpellNode(2.0f,"Big Explosion"))

        node("poison", PotionSpellNode(StatusEffects.POISON))
        node("wither", PotionSpellNode(StatusEffects.WITHER))
        node("levitation", PotionSpellNode(StatusEffects.LEVITATION))
        node("slowness", PotionSpellNode(StatusEffects.SLOWNESS))
        node("speed", PotionSpellNode(StatusEffects.SPEED))
        node("jump_boost", PotionSpellNode(StatusEffects.JUMP_BOOST))
        node("invisibility", PotionSpellNode(StatusEffects.INVISIBILITY))

        node("bomb", TurretSpellNode(60, 1,"Bomb"))
        node("long_bomb", TurretSpellNode(200, 1,"Long Bomb"))
        node("turret", TurretSpellNode(20, 5,"Turret"))

        node("zone", ZoneSpellNode(Vec3d(3.0,3.0,3.0),"Zone"))
        node("large_zone", ZoneSpellNode(Vec3d(6.0,3.0,6.0),"Large Zone"))
        node("giant_zone", ZoneSpellNode(Vec3d(12.0,5.0,12.0),"Giant Zone"))

        node("trap", TrapSpellNode(1f, 1,"Trap"))
        node("multi_trap", TrapSpellNode(1f, 5,"Multi Trap"))
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