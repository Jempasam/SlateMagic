package slatemagic.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import slatemagic.block.entity.fetchSpellPart
import slatemagic.spell.SpellContext
import slatemagic.spell.build.SPELL
import slatemagic.spell.build.invoke

class ActivatorBlock(settings: Settings, val smart: Boolean): Block(settings) {

    companion object{
        val TRIGGERED=BooleanProperty.of("triggered")
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
                    val finalPower = if (smart) power else 1
                    for (direction in Direction.entries) {
                        val next_pos = pos.offset(direction)
                        try {
                            val (spell_pos, spell_direction, spellnode) = fetchSpellPart(world, next_pos, direction)
                            if (spellnode.type == SPELL) {
                                val spell = SPELL(spellnode).effect
                                spell.use(
                                    SpellContext.at(
                                        world,
                                        Vec3d.ofCenter(spell_pos.offset(spell_direction)),
                                        Vec2f(spell_direction.offsetY * -90f, spell_direction.asRotation()),
                                        finalPower
                                    )
                                )
                            }
                        } catch (e: Exception) { continue }
                    }
                    world.setBlockState(pos,state.with(TRIGGERED,true))
                    println("Triggered")
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