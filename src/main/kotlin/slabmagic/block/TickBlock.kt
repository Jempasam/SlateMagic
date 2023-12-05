package slabmagic.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.BlockView
import net.minecraft.world.World

class TickBlock(settings: Settings, val tickSpacing: Int, val randomness: Int) : Block(settings) {

    init {
        defaultState=defaultState.with(TRIGGERED,false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(TRIGGERED)
    }

    fun time(random: Random) = if(randomness>0) tickSpacing+random.nextInt(randomness*2)-randomness else tickSpacing

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        super.scheduledTick(state, world, pos, random)
        world.createAndScheduleBlockTick(pos, this, time(random))
        world.setBlockState(pos, state.with(TRIGGERED,!state.get(TRIGGERED)))
    }

    override fun emitsRedstonePower(state: BlockState) = true

    override fun getWeakRedstonePower(state: BlockState, world: BlockView, pos: BlockPos, direction: Direction): Int {
        return if(state.get(TRIGGERED)) 15 else 0
    }

    override fun getStrongRedstonePower(state: BlockState, world: BlockView, pos: BlockPos, direction: Direction): Int {
        return 0
    }

    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        super.onBlockAdded(state, world, pos, oldState, notify)
        world.createAndScheduleBlockTick(pos, this, time(Random.create()))
    }
}