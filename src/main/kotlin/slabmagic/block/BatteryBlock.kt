package slabmagic.block

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.util.dynamic.Codecs
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import org.joml.Vector3f
import slabmagic.block.properties.VisitableBlock
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.EnergyBlockParticleEffect
import slabmagic.particle.SlabMagicParticles
import slabmagic.spell.build.visited.SlabPartVisited
import slabmagic.spell.build.visitor.PartVisitor
import slabmagic.utils.coerceIn

class BatteryBlock(settings: Settings, val cadency: Int, val power: Int, val color: Vector3f) : Block(settings), VisitableBlock {
    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(LEVEL)
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random?) {
        super.scheduledTick(state, world, pos, random)
        world.scheduleBlockTick(pos, this, cadency)
        val level = state.get(LEVEL)
        if(level<7){
            world.setBlockState(pos, state.with(LEVEL,level+1))
        }
    }

    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        super.onBlockAdded(state, world, pos, oldState, notify)
        world.scheduleBlockTick(pos, this, cadency)
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
                            color.apply { add(.3f,.3f,.3f); coerceIn(0f,1f) },
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

    override fun getCodec() = CODEC

    companion object{
        val CODEC=RecordCodecBuilder.mapCodec{ it :RecordCodecBuilder.Instance<BatteryBlock> ->
            it.group(
                createSettingsCodec(),
                Codec.INT.fieldOf("cadency").forGetter { it.cadency },
                Codec.INT.fieldOf("power").forGetter { it.power },
                Codecs.VECTOR_3F.fieldOf("color").forGetter { it.color }
            ).apply(it,::BatteryBlock)
        }
    }
}