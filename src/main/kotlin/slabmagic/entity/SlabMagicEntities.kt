package slabmagic.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.EntityType.EntityFactory
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import slabmagic.SlabMagicMod

object SlabMagicEntities {


    val SPELL_TURRET = register("spell_turret", ::SpellTurretEntity){
        dimensions(1f,1f)
    }

    val SPELL_TRAP = register("spell_trap", ::SpellTrapEntity){
        dimensions(1f,1f)
    }

    val SPELL_PROJECTILE = register("spell_projectile", ::SpellProjectileEntity){
        dimensions(1f,1f)
    }

    val SPELL_CURSE = register("spell_curse", ::SpellCurseEntity){
        dimensions(1f,1f)
    }

    val SPELL_ENCHANTING = register("spell_enchanting", ::SpellEnchantingEntity){
        dimensions(1f,1f)
    }

    val SPELL_SHIELD = register("spell_shield", ::SpellShieldEntity){
        dimensions(1f,1f)
    }

    val BLOCK_FOLLOWING = register("block_following", ::BlockFollowingEntity){
        dimensions(1f,1f)
    }


    fun <T: Entity>register(id: String, factory: EntityFactory<T>, builder: EntityType.Builder<T>.()->Unit): EntityType<T> {
        val type=EntityType.Builder.create(factory,SpawnGroup.MISC).apply{builder()}.build()
        Registry.register(Registries.ENTITY_TYPE, SlabMagicMod.id(id), type)
        return type
    }
}