package slabmagic.entity

import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.BlockStateParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import slabmagic.entity.tracked.SlabMagicTrackedData
import slabmagic.entity.tracked.provideDelegate
import kotlin.jvm.optionals.getOrNull
import kotlin.random.Random

class BlockFollowingEntity(type: EntityType<*>, world: World, var duration: Int=20) : FollowingEntity(type, world) {

    companion object{
        val BLOCK = DataTracker.registerData(BlockFollowingEntity::class.java, SlabMagicTrackedData.BLOCK)
    }

    var block by BLOCK

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(BLOCK, Blocks.AIR)
    }

    override fun tickTarget(target: Entity) {
        if(!world.isClient){
            duration--
            if(duration<0) kill()
        }
        val block=block
        if(block!=Blocks.AIR){
            val bpos=BlockPos.ofFloored(target.pos)
            val state=block.defaultState
            block.onEntityCollision(state, world, bpos, target)
            block.onSteppedOn(world, bpos, state, target)
        }
    }

    override fun onRemoved() {
        super.onRemoved()
        if(world.isClient && block!=Blocks.AIR){
            val effect=BlockStateParticleEffect(ParticleTypes.BLOCK, block.defaultState)
            for(i in 0..20){
                val pos=pos
                world.addParticle(
                    effect,
                    pos.x-0.5+ Random.nextFloat(), pos.y-0.5+ Random.nextFloat(), pos.z-0.5+ Random.nextFloat(),
                    -0.5+ Random.nextFloat(), -0.5+ Random.nextFloat(), -0.5+ Random.nextFloat()
                )
            }
        }
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        block = nbt.getString("block") .let{Identifier.tryParse(it)} ?.let {Registries.BLOCK.getOrEmpty(it)} ?.getOrNull() ?: Blocks.AIR
        duration = nbt.getInt("duration")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        if(block!=Blocks.AIR) nbt.putString("block", Registries.BLOCK.getId(block).toString())
        nbt.putInt("duration",duration)
    }

    override fun pos(target: Entity) = target.pos.add(0.0, 0.5, 0.0)
}