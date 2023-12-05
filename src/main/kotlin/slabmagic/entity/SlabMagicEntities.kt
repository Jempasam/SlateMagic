package slabmagic.entity

import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.EntityType.EntityFactory
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.registry.Registry
import slabmagic.SlabMagicMod

object SlabMagicEntities {


    val SPELL_TURRET = register("spell_turret", ::SpellTurretEntity){
        dimensions(EntityType.FIREBALL.dimensions)
    }

    val SPELL_TRAP = register("spell_trap", ::SpellTrapEntity){
        dimensions(EntityType.FIREBALL.dimensions)
    }

    val SPELL_PROJECTILE = register("spell_projectile", ::SpellProjectileEntity){
        dimensions(EntityType.FIREBALL.dimensions)
    }

    val SPELL_CURSE = register("spell_curse", ::SpellCurseEntity){
        dimensions(EntityType.FIREBALL.dimensions)
    }

    val SPELL_ENCHANTING = register("spell_enchanting", ::SpellEnchantingEntity){
        dimensions(EntityType.FIREBALL.dimensions)
    }

    val SPELL_SHIELD = register("spell_shield", ::SpellShieldEntity){
        dimensions(EntityType.FIREBALL.dimensions)
    }


    fun <T: Entity>register(id: String, factory: EntityFactory<T>, builder: FabricEntityTypeBuilder<T>.()->Unit): EntityType<T> {
        val type=FabricEntityTypeBuilder.create(SpawnGroup.MISC,factory).apply{builder()}.build()
        Registry.register(Registry.ENTITY_TYPE, SlabMagicMod.id(id), type)
        return type
    }
}