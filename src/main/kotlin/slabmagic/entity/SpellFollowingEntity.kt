package slabmagic.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World
import slabmagic.entity.data.SpellEntity
import slabmagic.entity.tracked.SlabMagicTrackedData
import slabmagic.spell.build.AssembledSpell

abstract class SpellFollowingEntity(
    type: EntityType<*>,
    world: World,
    spells: List<AssembledSpell> = listOf(),
    power: Int = 1,
    range: Float = 1f
) : FollowingEntity(type, world, range), SpellEntity{

    companion object{
        val SPELL_DATA = DataTracker.registerData(SpellFollowingEntity::class.java, SlabMagicTrackedData.SPELL_DATA)
    }

    init{
        spellData.spells=spells
        spellData.power=power
    }

    final override val spellData get() = dataTracker[SPELL_DATA]

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(SPELL_DATA, SpellEntity.Data())
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        spellData.read(nbt)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        spellData.write(nbt)
    }
}