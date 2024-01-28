package slabmagic.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.Packet
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import slabmagic.entity.data.SpellEntity
import slabmagic.entity.tracked.SlabMagicTrackedData
import slabmagic.spell.build.AssembledSpell
import kotlin.math.abs


open class SimpleSpellEntity(type: EntityType<*>, world: World) : Entity(type, world), SpellEntity {


    companion object{
        val SPELL_DATA = DataTracker.registerData(SimpleSpellEntity::class.java, SlabMagicTrackedData.SPELL_DATA)
    }


    constructor(type: EntityType<*>, world: World, spells: List<AssembledSpell>, power: Int) : this(type, world) {
        spellData.spells=spells
        spellData.power=power
    }


    final override val spellData get() = dataTracker[SPELL_DATA]

    override fun tick() {
        super.tick()
        setPosition(pos.x+velocity.x,pos.y+velocity.y,pos.z+velocity.z)
        velocity=velocity.multiply(0.3)
        if(abs(velocity.x) <0.01 && abs(velocity.y) <0.01 && abs(velocity.z) <0.01)velocity= Vec3d.ZERO
        velocityDirty=true
        scheduleVelocityUpdate()
    }

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