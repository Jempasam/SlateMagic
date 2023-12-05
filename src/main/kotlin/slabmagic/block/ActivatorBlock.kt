package slabmagic.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import slabmagic.block.entity.visitAt
import slabmagic.spell.build.parts.SPELL
import slabmagic.spell.build.visitor.AssemblingNodeVisitor
import slabmagic.spell.build.visitor.VisitorException

class ActivatorBlock(settings: Settings, val smart: Boolean): Block(settings) {

    init {
        defaultState=defaultState.with(TRIGGERED,false)
    }

    @Deprecated("Deprecated in Java")
    override fun neighborUpdate(
        state: BlockState,
        world: World,
        pos: BlockPos,
        sourceBlock: Block,
        sourcePos: BlockPos,
        notify: Boolean
    ) {
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify)
        if (!world.isClient) {
            world as ServerWorld
            val power=world.getReceivedRedstonePower(pos)
            val triggered=state.get(TRIGGERED)

            if (power>0) {
                if(!triggered) {
                    val finalPower = if (smart) power else 1
                    for (direction in Direction.entries) {
                        val next_pos = pos.offset(direction)

                        val visitor= AssemblingNodeVisitor()
                        try{
                            visitor.visitAt(world,next_pos,direction)
                        }catch (e: VisitorException){ }

                        val result=visitor.result
                        val visited=visitor.lastVisited
                        if(result!=null && visited!=null && result.first.type==SPELL){
                            val spell=SPELL.get(result.first)
                            visited.cast(spell.effect,finalPower)
                        }
                    }
                    world.setBlockState(pos,state.with(TRIGGERED,true))
                }
            }
            else if(triggered) world.setBlockState(pos,state.with(TRIGGERED,false))

        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(TRIGGERED)
    }
}