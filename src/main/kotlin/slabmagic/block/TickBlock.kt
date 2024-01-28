package slabmagic.block

import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random

class TickBlock(settings: Settings, val tickSpacing: Int, val randomness: Int) : AbstractTickBlock(settings) {

    override fun time(random: Random) = if(randomness>0) tickSpacing+random.nextInt(randomness*2)-randomness else tickSpacing

    override fun test(state: BlockState, world: ServerWorld, pos: BlockPos) = !state.get(TRIGGERED)

}