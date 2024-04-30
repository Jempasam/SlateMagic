package slabmagic.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World
import slabmagic.entity.data.SpellEntity
import slabmagic.entity.tracked.SlabMagicTrackedData
import slabmagic.entity.tracked.provideDelegate

abstract class SpellFollowingEntity(
    type: EntityType<*>,
    world: World,
    data: SpellEntity.Data = SpellEntity.Data.EMPTY,
    range: Float = 1f
) : FollowingEntity(type, world, range), SpellEntity{

    companion object{
        val SPELL_DATA = DataTracker.registerData(SpellFollowingEntity::class.java, SlabMagicTrackedData.SPELL_DATA)
    }

    final override var spellData by SPELL_DATA

    init{
        spellData=data
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(SPELL_DATA, SpellEntity.Data())
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