package slabmagic.block

import com.google.common.base.Predicates
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.ai.TargetPredicate
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.world.BlockView
import net.minecraft.world.World

class PlayerDetectorBlock(val type: Class<out Entity>, val range: Double, val cadency: Int, settings: Settings?): Block(settings) {

    init {
        defaultState=defaultState.with(TRIGGERED,false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(TRIGGERED)
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        super.scheduledTick(state, world, pos, random)
        world.createAndScheduleBlockTick(pos, this, cadency)

        val box = Box.of(Vec3d.ofCenter(pos),range,range,range)
        val finded =
            if(type==PlayerEntity::class.java) world.getPlayers(TargetPredicate.DEFAULT, null, box)
            else world.getEntitiesByClass(type, box, Predicates.alwaysTrue())

        if(finded.size>0){
            world.setBlockState(pos, state.with(TRIGGERED,true))
        }
        else{
            world.setBlockState(pos, state.with(TRIGGERED,false))
        }
    }

    override fun emitsRedstonePower(state: BlockState) = true

    override fun getWeakRedstonePower(state: BlockState, world: BlockView, pos: BlockPos, direction: Direction): Int {
        return if(state.get(TRIGGERED)) 15 else 0
    }

    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        super.onBlockAdded(state, world, pos, oldState, notify)
        world.createAndScheduleBlockTick(pos, this, cadency)
    }
}