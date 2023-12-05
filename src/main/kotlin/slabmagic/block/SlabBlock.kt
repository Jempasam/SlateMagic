package slabmagic.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import slabmagic.block.entity.PartBlockEntity
import slabmagic.item.NodeBlockItem
import slabmagic.item.SlabMagicItems

class SlabBlock(val factory: FabricBlockEntityTypeBuilder.Factory<out PartBlockEntity>, settings: Settings) : Block(settings), BlockEntityProvider {
    override fun createBlockEntity(pos: BlockPos, state: BlockState) = factory.create(pos, state)

    override fun getPickStack(world: BlockView, pos: BlockPos, state: BlockState): ItemStack {
        val ret=super.getPickStack(world,pos,state)
        val be=world.getBlockEntity(pos)
        if(be is PartBlockEntity){
            be.part?.let { node -> SlabMagicItems.SLAB.setNode(ret,node) }
        }
        return ret
    }

    override fun onStacksDropped(state: BlockState, world: ServerWorld, pos: BlockPos, stack: ItemStack, dropExperience: Boolean) {
        val item=stack.item
        if(item is NodeBlockItem){
            val be=world.getBlockEntity(pos)
            if(be is PartBlockEntity){
                be.part?.let { node -> SlabMagicItems.SLAB.setNode(stack,node) }
            }
        }
    }

}