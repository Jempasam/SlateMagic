package slabmagic.datagen

import slabmagic.datagen.info.BlockInfo
import slabmagic.datagen.info.ItemInfo
import slabmagic.item.SlabMagicItems

object SlabMagicInfos {
    val blockInfos= mutableListOf<BlockInfo>()
    val itemInfos= mutableListOf<ItemInfo>()

    operator fun BlockInfo.unaryPlus() = blockInfos.add(this)
    operator fun ItemInfo.unaryPlus() = itemInfos.add(this)

    init{
        +ItemInfo(SlabMagicItems.ULTRA_WAND)
    }
}