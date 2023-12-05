package slabmagic.block.entity

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.registry.Registry
import slabmagic.SlabMagicMod
import slabmagic.block.SlabMagicBlocks

object SlabMagicBlockEntities {

    val SPELL_PART: BlockEntityType<PartBlockEntity> = create("slab", arrayOf(SlabMagicBlocks.SLAB), PartBlockEntity::factory)

    val SPELL: BlockEntityType<SpellBlockEntity> = create("spell", arrayOf(SlabMagicBlocks.SLAB), SpellBlockEntity::factory)

    fun <T: BlockEntity> create(id: String, blocks: Array<Block>, factory: FabricBlockEntityTypeBuilder.Factory<T>): BlockEntityType<T>{
        val type=FabricBlockEntityTypeBuilder.create(factory, *blocks).build()
        Registry.register(Registry.BLOCK_ENTITY_TYPE, SlabMagicMod.id(id), type)
        return type
    }
}