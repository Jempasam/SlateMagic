package slatemagic.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.Packet
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.world.World
import slatemagic.entity.data.SpellEntity
import slatemagic.entity.tracked.SlateMagicTrackedData
import slatemagic.spell.effect.SpellEffect


open class SimpleSpellEntity(type: EntityType<*>, world: World) : Entity(type, world), SpellEntity {


    companion object{
        val SPELL_DATA = DataTracker.registerData(SimpleSpellEntity::class.java, SlateMagicTrackedData.SPELL_DATA)
    }


    constructor(type: EntityType<*>, world: World, spell: SpellEffect, power: Int) : this(type, world) {
        spellData.spell=spell
        spellData.power=power
    }


    final override val spellData get() = dataTracker[SPELL_DATA]

    override fun initDataTracker() {
        dataTracker.startTracking(SPELL_DATA, SpellEntity.Data())
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        spellData.read(nbt)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        spellData.write(nbt)
    }

    override fun createSpawnPacket(): Packet<*> = EntitySpawnS2CPacket(this)

}