package slabmagic.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.projectile.thrown.ThrownEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import slabmagic.entity.data.SpellEntity
import slabmagic.entity.tracked.SlabMagicTrackedData
import slabmagic.particle.MagicParticleEffect
import slabmagic.spell.SpellContext
import slabmagic.spell.build.AssembledSpell

class SpellProjectileEntity : ThrownEntity, SpellEntity {


    companion object{
        val SPELL_DATA = DataTracker.registerData(SimpleSpellEntity::class.java, SlabMagicTrackedData.SPELL_DATA)
    }


    private var maxage=0


    constructor(type: EntityType<out SpellProjectileEntity>, world: World)
            : super(type, world)

    constructor(type: EntityType<out SpellProjectileEntity>, world: World, spell: AssembledSpell, power: Int, maxage: Int)
            : super(type, world)
    {
        this.maxage = maxage
        this.spellData.spell=spell
        this.spellData.power=power
    }

    fun setVelocity(pitch: Float, yaw: Float, speed: Float, divergence: Float) {
        val f = -MathHelper.sin(yaw * 0.017453292f) * MathHelper.cos(pitch * 0.017453292f)
        val g = -MathHelper.sin(pitch * 0.017453292f)
        val h = MathHelper.cos(yaw * 0.017453292f) * MathHelper.cos(pitch * 0.017453292f)
        this.setVelocity(f.toDouble(), g.toDouble(), h.toDouble(), speed, divergence)
    }


    override fun tick() {
        super.tick()
        if(world.isClient){
            world.addParticle(MagicParticleEffect(spell.color, 0.3f), x, y, z, 0.0, 0.0, 0.0)
        }
        else{
            if(age>maxage){
                onPos(pos)
            }
        }
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        super.onEntityHit(entityHitResult)
        if(!world.isClient){
            val context=SpellContext.at(entityHitResult.entity, power)
            context.markeds.addAll(markeds)
            context.direction= Vec2f(-pitch, -yaw)
            spell.use(context)
            kill()
        }
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        super.onBlockHit(blockHitResult)
        val rotation=Vec2f(-pitch,-yaw)
        val pos=blockHitResult.blockPos.add(blockHitResult.side.vector)
        onPos(Vec3d.ofCenter(pos))
    }

    fun onPos(pos: Vec3d){
        val rotation=Vec2f(-pitch,-yaw)
        if(!world.isClient) {
            val ctx=SpellContext.at(world as ServerWorld, pos, rotation, power)
            ctx.markeds.addAll(markeds)
            spell.use(ctx)
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