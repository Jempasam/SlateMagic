package slabmagic.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import slabmagic.block.entity.NodeBlockEntity
import slabmagic.block.entity.SlabBlockEntity
import slabmagic.item.SlabMagicItems

class SlabBlock(val factory: FabricBlockEntityTypeBuilder.Factory<out SlabBlockEntity>, settings: Settings) : Block(settings), BlockEntityProvider {
    override fun createBlockEntity(pos: BlockPos, state: BlockState) = factory.create(pos, state)

    override fun getPickStack(world: BlockView, pos: BlockPos, state: BlockState): ItemStack {
        val ret=super.getPickStack(world,pos,state)
        val be=world.getBlockEntity(pos)
        if(be is NodeBlockEntity){
            be.node?.let { node -> SlabMagicItems.SLAB.setNode(ret,node) }
        }
        return ret
    }
}