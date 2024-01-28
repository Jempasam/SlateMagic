package slabmagic.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import slabmagic.block.properties.VisitableBlock
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.EnergyBlockParticleEffect
import slabmagic.particle.SlabMagicParticles
import slabmagic.spell.build.visited.SlabPartVisited
import slabmagic.spell.build.visitor.PartVisitor

class BatteryBlock(settings: Settings, val cadency: Int, val power: Int, val color: Vec3f) : Block(settings), VisitableBlock {
    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(LEVEL)
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random?) {
        super.scheduledTick(state, world, pos, random)
        world.createAndScheduleBlockTick(pos, this, cadency)
        val level = state.get(LEVEL)
        if(level<7){
            world.setBlockState(pos, state.with(LEVEL,level+1))
        }
    }

    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        super.onBlockAdded(state, world, pos, oldState, notify)
        world.createAndScheduleBlockTick(pos, this, cadency)
    }

    override fun hasComparatorOutput(state: BlockState?) = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos) = state.get(LEVEL)*2

    override fun visit(visitor: PartVisitor, visited: SlabPartVisited): SlabPartVisited {
        val world=visited.block.world
        val pos=visited.block.pos
        val state=world.getBlockState(pos)
        val level=state.get(LEVEL)

        val oldc=visited.consumer
        return visited.copy(
            energy=visited.energy+level*power,
            consumer = {
                oldc()
                if(world is ServerWorld){
                    sendParticleEffect(
                        world,
                        EnergyBlockParticleEffect(
                            SlabMagicParticles.CUBE_ELECTRIC,
                            color.apply { add(.3f,.3f,.3f); clamp(0f,1f) },
                            color,
                            0.5f
                        ),
                        Vec3d.ofCenter(visited.block.pos)
                    )
                }
                world.setBlockState(pos, state.with(LEVEL,0))
            }
        )
    }
}