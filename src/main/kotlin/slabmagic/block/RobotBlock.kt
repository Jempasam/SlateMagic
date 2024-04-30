package slabmagic.block

import com.google.common.base.Predicates
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import slabmagic.block.properties.visitAt
import slabmagic.item.WandItem
import slabmagic.spell.build.parts.SPELL
import slabmagic.spell.build.visitor.AssemblingPartVisitor
import slabmagic.spell.build.visitor.VisitorException
import kotlin.math.abs
import kotlin.math.max

class RobotBlock(settings: Settings, val action: WandItem.Action, val power: Int, val range: Double, val speed: Int) : HorizontalFacingBlock(settings) {


    init {
        defaultState=defaultState.with(TRIGGERED,false).with(FACING,Direction.NORTH)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return defaultState.with(FACING, ctx.playerLookDirection.opposite) as BlockState
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(TRIGGERED)
        builder.add(FACING)
    }



    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        super.scheduledTick(state, world, pos, random)

        // Search for a players around
        val box = Box.of(Vec3d.ofCenter(pos),range,range,range)
        val findeds = world.getEntitiesByClass(PlayerEntity::class.java, box, Predicates.alwaysTrue())

        // If there is a player around, trigger the robot
        if(findeds.isEmpty()){
            // On nothing found
            world.setBlockState(pos, state.with(TRIGGERED,false))
            world.scheduleBlockTick(pos, this, max(200,speed))
        }
        else{
            // On player found
            world.setBlockState(pos, state.with(TRIGGERED,true))
            world.scheduleBlockTick(pos, this, speed)

            // Get direction
            val player= findeds[0]
            val offset= player.pos.subtract(Vec3d.ofCenter(pos))
            val max= max(abs(offset.x), abs(offset.z))
            val x= offset.x/max
            val z= offset.z/max
            val direction= when{
                z> 0.99 -> Direction.SOUTH
                z< -0.99 -> Direction.NORTH
                x> 0.99 -> Direction.EAST
                else -> Direction.WEST
            }

            // Try side activation
            val next_pos = pos.offset(direction)
            val visitor= AssemblingPartVisitor()
            try{
                val visited= visitor.visitAt(world,next_pos,direction)
                if(visitor.result==null) throw Exception()
                val result=visitor.result
                if(result!=null && result.first.type==SPELL){
                    val spell= SPELL.get(result.first)
                    action.apply(visited, null, spell, power)
                }
            }
            // If don't succeed, try top activation
            catch (_: Exception){
                val next_pos = pos.offset(Direction.UP)
                val visitor= AssemblingPartVisitor()
                try{
                    val visited= visitor.visitAt(world,next_pos,direction)
                    val result=visitor.result
                    if(result!=null && result.first.type==SPELL){
                        val spell= SPELL.get(result.first)
                        action.apply(visited, null, spell, power)
                    }
                }catch (_: VisitorException){}
            }
        }
    }

    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        if(world is ServerWorld) scheduledTick(state, world, pos, Random.create())
    }

    override fun getCodec() = CODEC

    companion object{
        val CODEC= RecordCodecBuilder.mapCodec<RobotBlock>{
            it.group(
                createSettingsCodec(),
                Codec.INT.optionalFieldOf("power",1).forGetter { it.power },
                Codec.DOUBLE.fieldOf("range").forGetter { it.range },
                Codec.INT.fieldOf("speed").forGetter { it.speed },
            ).apply(it){a,b,c,d->RobotBlock(a,WandItem::cast, b, c, d)}
        }
    }
}