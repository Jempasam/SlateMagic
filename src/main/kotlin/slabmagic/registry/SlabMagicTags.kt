package slabmagic.registry

import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey
import slabmagic.SlabMagicMod

object SlabMagicTags {

    @JvmStatic
    val LENS=item("lens")

    fun item(id: String) = TagKey.of(Registries.ITEM.key,SlabMagicMod.id(id))
}