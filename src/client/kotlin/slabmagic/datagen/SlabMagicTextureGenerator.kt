package slabmagic.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.registry.RegistryWrapper
import slabmagic.datagen.tools.TextureProvider
import java.util.concurrent.CompletableFuture

class SlabMagicTextureGenerator(output: FabricDataOutput, registries: CompletableFuture<RegistryWrapper.WrapperLookup>) : TextureProvider(output,registries) {
    override fun generateTextures(gen: TextureGenerator, registries: RegistryWrapper.WrapperLookup) = gen.run {
        /*val tex = texture(SlabMagicMod/"block/robot/copper_front_powered")
        val result = builder()
            .paint(tex)
            .apply(sTint(Vector3f(0.6f, 0.56f, 0.55f),0.3f), tTint(Vector3f(0.82f, 0.57f, 0f)))
            .apply(sHue(Vector3f(1f, 0.3f, 0f),0.4f), tTint(Vector3f(0.3f,0.3f,0.3f)))
        save(SlabMagicMod/"block/robot/test_front", result)*/
    }
}