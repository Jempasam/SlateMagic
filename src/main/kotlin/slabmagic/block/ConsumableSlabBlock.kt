package slabmagic.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.Blocks
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d
import slabmagic.block.entity.PartBlockEntity
import slabmagic.helper.ColorTools
import slabmagic.network.messages.sendSimpleParticleEffect
import slabmagic.particle.MagicParticleEffect
import slabmagic.spell.build.visited.SlabPartVisited
import slabmagic.spell.build.visitor.PartVisitor

class ConsumableSlabBlock(factory: FabricBlockEntityTypeBuilder.Factory<out PartBlockEntity>, settings: Settings) : SlabBlock(factory,settings) {

    override fun visit(visitor: PartVisitor, visited: SlabPartVisited): SlabPartVisited {
        val bentity=visited.block.world.getBlockEntity(visited.block.pos)
        if(bentity is PartBlockEntity){
            bentity.part?.let { visitor.visit(visited,it) }
        }
        val oldc=visited.consumer
        return visited.copy(
            consumer = {
                oldc()
                val world=visited.block.world
                if(world is ServerWorld){
                    sendSimpleParticleEffect(
                        world,
                        MagicParticleEffect(ColorTools.WHITE,0.5f),
                        Vec3d.ofCenter(visited.block.pos),
                        speed = 0.5, count = 5
                    )
                    visited.block.world.setBlockState(visited.block.pos, Blocks.AIR.defaultState)
                }
            }
        )
    }

}