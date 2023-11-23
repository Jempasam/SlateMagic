package slatemagic.io.loader

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resource.ResourceType

object SlateMagicLoaders {

    val SPELL_NODE=register(SpellNodeLoader)
    fun <T: IdentifiableResourceReloadListener> register(loader: T): T{
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(loader)
        return loader
    }
}