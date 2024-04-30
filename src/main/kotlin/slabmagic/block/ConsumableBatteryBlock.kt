package slabmagic.block

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.dynamic.Codecs
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f
import slabmagic.block.properties.VisitableBlock
import slabmagic.network.messages.sendParticleEffect
import slabmagic.network.messages.sendSimpleParticleEffect
import slabmagic.particle.EnergyBlockParticleEffect
import slabmagic.particle.MagicParticleEffect
import slabmagic.particle.SlabMagicParticles
import slabmagic.spell.build.visited.SlabPartVisited
import slabmagic.spell.build.visitor.PartVisitor
import slabmagic.utils.coerceIn

class ConsumableBatteryBlock(settings: Settings, val power: Int, val color: Vector3f) : Block(settings), VisitableBlock {

    override fun visit(visitor: PartVisitor, visited: SlabPartVisited): SlabPartVisited {
        val world=visited.block.world
        val pos=visited.block.pos
        val state=world.getBlockState(pos)

        val oldc=visited.consumer
        return visited.copy(
            energy=visited.energy+power,
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
                    sendSimpleParticleEffect(
                        world,
                        MagicParticleEffect(color,0.5f),
                        Vec3d.ofCenter(visited.block.pos),
                        speed = 0.5, count = 5
                    )
                    world.setBlockState(pos, Blocks.AIR.defaultState)
                }
            }
        )
    }

    override fun getCodec() = CODEC

    companion object{
        val CODEC= RecordCodecBuilder.mapCodec{it :RecordCodecBuilder.Instance<ConsumableBatteryBlock> ->
            it.group(
                createSettingsCodec(),
                Codec.INT.fieldOf("power").forGetter { it.power },
                Codecs.VECTOR_3F.fieldOf("color").forGetter { it.color }
            ).apply(it,::ConsumableBatteryBlock)
        }
    }
}