package slatemagic.block.entity

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.registry.Registry
import slatemagic.SlateMagicMod
import slatemagic.block.SlateMagicBlocks

object SlateMagicBlockEntities {

    val SLATE_BLOCK: BlockEntityType<NodeBlockEntity> = create("slate_block", arrayOf(SlateMagicBlocks.SLATE_BLOCK), ::SlateBlockEntity)

    fun <T: BlockEntity> create(id: String, blocks: Array<Block>, factory: FabricBlockEntityTypeBuilder.Factory<T>): BlockEntityType<T>{
        val type=FabricBlockEntityTypeBuilder.create(factory, *blocks).build()
        Registry.register(Registry.BLOCK_ENTITY_TYPE, SlateMagicMod.id(id), type)
        return type
    }
}