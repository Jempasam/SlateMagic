package slabmagic.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import slabmagic.block.properties.VisitableBlock
import slabmagic.spell.build.visited.SlabPartVisited
import slabmagic.spell.build.visitor.PartVisitor

class ConductorBlock(settings: Settings) : Block(settings), VisitableBlock {

    init {
        defaultState=defaultState.with(TRIGGERED,false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(TRIGGERED)
    }

    override fun neighborUpdate(state: BlockState, world: World, pos: BlockPos, sourceBlock: Block, sourcePos: BlockPos, notify: Boolean) {
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify)
        if(world.isReceivingRedstonePower(pos)){
            world.setBlockState(pos, state.with(TRIGGERED,true))
        }
        else{
            world.setBlockState(pos, state.with(TRIGGERED,false))
        }
    }

    override fun visit(visitor: PartVisitor, visited: SlabPartVisited): SlabPartVisited? {
        return visited.takeIf { visited.block.state.get(TRIGGERED) }
    }

}