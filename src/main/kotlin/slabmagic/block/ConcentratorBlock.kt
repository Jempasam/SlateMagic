package slabmagic.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import slabmagic.block.entity.visitAt
import slabmagic.helper.ColorTools
import slabmagic.spell.build.parts.AssembledSpell
import slabmagic.spell.build.parts.SPELL
import slabmagic.spell.build.visitor.AssemblingNodeVisitor
import slabmagic.spell.build.visitor.Visited
import slabmagic.spell.build.visitor.VisitorException

class ConcentratorBlock(val action: Action, settings: Settings): Block(settings) {

    fun interface Action{
        fun apply(visited: Visited, spell: AssembledSpell, power: Int)
    }

    companion object{
        val TRIGGERED=BooleanProperty.of("triggered")

        fun castPower(visited: Visited, spell: AssembledSpell, power: Int){
            visited.cast(spell.effect,power)
        }

        fun cast(visited: Visited, spell: AssembledSpell, power: Int){
            visited.cast(spell.effect,1)
        }

        fun show(visited: Visited, spell: AssembledSpell, power: Int){
            visited.show(spell.effect.shape,ColorTools.int(spell.effect.color))
        }
    }

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
                            action.apply(visited,spell,power)
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