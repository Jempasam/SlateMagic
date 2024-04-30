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

abstract class AbstractTickBlock(settings: Settings) : Block(settings) {

    init {
        defaultState=defaultState.with(TRIGGERED,false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(TRIGGERED)
    }

    abstract fun time(random: Random): Int
    abstract fun test(state: BlockState, world: ServerWorld, pos: BlockPos): Boolean

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        super.scheduledTick(state, world, pos, random)

        if(world.getEmittedRedstonePower(pos.down(),Direction.DOWN)>0){
            world.setBlockState(pos, state.with(TRIGGERED,false))
            return
        }
        
        world.scheduleBlockTick(pos, this, time(random))
        world.setBlockState(pos, state.with(TRIGGERED,test(state,world,pos)))
    }

    override fun emitsRedstonePower(state: BlockState) = true

    override fun getWeakRedstonePower(state: BlockState, world: BlockView, pos: BlockPos, direction: Direction): Int {
        return if(state.get(TRIGGERED)) 15 else 0
    }

    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        super.onBlockAdded(state, world, pos, oldState, notify)
        if(world.getEmittedRedstonePower(pos.down(),Direction.DOWN)==0) world.scheduleBlockTick(pos, this, time(Random.create()))
        else if(state.get(TRIGGERED))world.setBlockState(pos, state.with(TRIGGERED,false))
    }

    override fun neighborUpdate(state: BlockState, world: World, pos: BlockPos, sourceBlock: Block, sourcePos: BlockPos, notify: Boolean) {
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify)
        if(world.getEmittedRedstonePower(pos.down(),Direction.DOWN)==0) world.scheduleBlockTick(pos, this, time(Random.create()))
        else if(state.get(TRIGGERED))world.setBlockState(pos, state.with(TRIGGERED,false))
    }
}