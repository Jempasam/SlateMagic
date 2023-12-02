package slabmagic.registry

import net.minecraft.tag.TagKey
import net.minecraft.util.registry.Registry
import slabmagic.SlabMagicMod

object SlabMagicTags {

    @JvmStatic
    val LENS=item("lens")

    fun item(id: String) = TagKey.of(Registry.ITEM.key,SlabMagicMod.id(id))
}