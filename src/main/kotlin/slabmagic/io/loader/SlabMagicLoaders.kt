package slabmagic.io.loader

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resource.ResourceType

object SlabMagicLoaders {

    fun <T: IdentifiableResourceReloadListener> register(loader: T): T{
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(loader)
        return loader
    }
}