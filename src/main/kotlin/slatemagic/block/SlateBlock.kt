package slatemagic.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import slatemagic.block.entity.NodeBlockEntity
import slatemagic.block.entity.SlateBlockEntity
import slatemagic.item.SlateMagicItems

class SlateBlock(val factory: FabricBlockEntityTypeBuilder.Factory<out SlateBlockEntity>, settings: Settings) : Block(settings), BlockEntityProvider {
    override fun createBlockEntity(pos: BlockPos, state: BlockState) = factory.create(pos, state)

    override fun getPickStack(world: BlockView, pos: BlockPos, state: BlockState): ItemStack {
        val ret=super.getPickStack(world,pos,state)
        val be=world.getBlockEntity(pos)
        if(be is NodeBlockEntity){
            be.node?.let { node -> SlateMagicItems.SLATE.setNode(ret,node) }
        }
        return ret
    }
}