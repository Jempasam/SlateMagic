package slatemagic.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.projectile.thrown.ThrownEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import slatemagic.entity.data.SpellEntity
import slatemagic.entity.tracked.SlateMagicTrackedData
import slatemagic.particle.MagicParticleEffect
import slatemagic.spell.Spell
import slatemagic.spell.SpellContext

class SpellProjectileEntity : ThrownEntity, SpellEntity {


    companion object{
        val SPELL_DATA = DataTracker.registerData(SimpleSpellEntity::class.java, SlateMagicTrackedData.SPELL_DATA)
    }


    private var maxage=0


    constructor(type: EntityType<out SpellProjectileEntity>, world: World)
            : super(type, world)

    constructor(type: EntityType<out SpellProjectileEntity>, world: World, spell: Spell, power: Int, maxage: Int)
            : super(type, world)
    {
        this.maxage = maxage
        this.spellData.spell=spell
        this.spellData.power=power
    }

    override fun tick() {
        super.tick()
        if(world.isClient){
            if(age%5==0) world.addParticle(MagicParticleEffect(spell.color, 0.3f), x, y, z, 0.0, 0.0, 0.0)
        }
        else{
            if(age>maxage) kill()
        }
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        super.onEntityHit(entityHitResult)
        if(!world.isClient){
            spell.use(SpellContext.at(entityHitResult.entity, power))
            kill()
        }
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        super.onBlockHit(blockHitResult)
        val rotation=Vec2f(-pitch,yaw)
        if(!world.isClient) {
            spell.use(SpellContext.at(world as ServerWorld, Vec3d.ofCenter(blockHitResult.blockPos), rotation, power))
            kill()
        }
    }

    override val spellData get() = dataTracker[SPELL_DATA]

    override fun initDataTracker() {
        dataTracker.startTracking(SPELL_DATA, SpellEntity.Data())
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        age=nbt.getInt("age")
        maxage=nbt.getInt("maxage")
        spellData.read(nbt)

    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt("age",age)
        nbt.putInt("maxage",maxage)
        spellData.write(nbt)
    }

}