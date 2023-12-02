package slabmagic.block.entity

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.registry.Registry
import slabmagic.SlabMagicMod
import slabmagic.block.SlabMagicBlocks

object SlabMagicBlockEntities {

    val SLAB: BlockEntityType<NodeBlockEntity> = create("slab", arrayOf(SlabMagicBlocks.SLAB), ::SlabBlockEntity)

    fun <T: BlockEntity> create(id: String, blocks: Array<Block>, factory: FabricBlockEntityTypeBuilder.Factory<T>): BlockEntityType<T>{
        val type=FabricBlockEntityTypeBuilder.create(factory, *blocks).build()
        Registry.register(Registry.BLOCK_ENTITY_TYPE, SlabMagicMod.id(id), type)
        return type
    }
}