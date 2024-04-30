package slabmagic.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldView
import slabmagic.block.entity.PartBlockEntity
import slabmagic.block.properties.VisitableBlock
import slabmagic.components.SlabMagicComponents
import slabmagic.spell.build.visited.SlabPartVisited
import slabmagic.spell.build.visitor.PartVisitor

open class SlabBlock(val factory: FabricBlockEntityTypeBuilder.Factory<out PartBlockEntity>, settings: Settings) : Block(settings), BlockEntityProvider, VisitableBlock {

    override fun createBlockEntity(pos: BlockPos, state: BlockState) = factory.create(pos, state)

    override fun getPickStack(world: WorldView, pos: BlockPos, state: BlockState): ItemStack {
        val ret=super.getPickStack(world, pos, state)
        world.getBlockEntity(pos) ?.components ?.get(SlabMagicComponents.PART) ?.let {
            ret.set(SlabMagicComponents.PART, it)
        }
        return ret
    }

    override fun visit(visitor: PartVisitor, visited: SlabPartVisited): SlabPartVisited {
        val part=visited.block.world.getBlockEntity(visited.block.pos)
            ?.components?.get(SlabMagicComponents.PART)
        if(part!=null){
            visitor.visit(visited,part.value())
        }
        return visited
    }

}