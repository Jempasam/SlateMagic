package slabmagic

import assets.`slab-magic`.block.SlabMagicBlockColors
import assets.`slab-magic`.block.SlabMagicItemColors
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.render.RenderLayer
import slabmagic.block.SlabMagicBlocks
import slabmagic.command.SlabMagicClientCommands
import slabmagic.entity.SlabMagicRenderers
import slabmagic.network.SlabMagicClientNetwork
import slabmagic.particle.SlabMagicClientParticles
import slabmagic.spell.effect.SpellEffect
import slabmagic.ui.GlassesHudRenderCallback

object SlabMagicModClient : ClientModInitializer {

	var spell: SpellEffect?=null
	var lastTime: Long=0

	override fun onInitializeClient() {
		SlabMagicClientParticles
		SlabMagicClientCommands
		SlabMagicClientParticles
		SlabMagicClientNetwork
		SlabMagicRenderers
		SlabMagicBlockColors
		SlabMagicItemColors
		HudRenderCallback.EVENT.register(GlassesHudRenderCallback)

		BlockRenderLayerMap.INSTANCE.putBlock(SlabMagicBlocks.SLAB,RenderLayer.getCutout())
		BlockRenderLayerMap.INSTANCE.putBlock(SlabMagicBlocks.OLD_SLAB,RenderLayer.getCutout())
		BlockRenderLayerMap.INSTANCE.putBlock(SlabMagicBlocks.ENERGY_SLAB,RenderLayer.getTranslucent())
		BlockRenderLayerMap.INSTANCE.putBlock(SlabMagicBlocks.ENERGY_BATTERY,RenderLayer.getTranslucent())
		BlockRenderLayerMap.INSTANCE.putBlock(SlabMagicBlocks.CONTAMINATED_REDSTONE_HEART,RenderLayer.getCutout())
		BlockRenderLayerMap.INSTANCE.putBlock(SlabMagicBlocks.CONTAMINATED_REDSTONE_EYE,RenderLayer.getCutout())

		SlabMagicBlocks.COPPER_GRATE.forEach{BlockRenderLayerMap.INSTANCE.putBlock(it,RenderLayer.getCutout())}
		SlabMagicBlocks.COPPER_WINDOW.forEach{BlockRenderLayerMap.INSTANCE.putBlock(it,RenderLayer.getCutout())}
		SlabMagicBlocks.COPPER_BRICKS.forEach{BlockRenderLayerMap.INSTANCE.putBlock(it,RenderLayer.getCutout())}
		SlabMagicBlocks.CHISELED_COPPER.forEach{BlockRenderLayerMap.INSTANCE.putBlock(it,RenderLayer.getCutout())}
		SlabMagicBlocks.METAL_SANDWICH.forEach{BlockRenderLayerMap.INSTANCE.putBlock(it,RenderLayer.getCutout())}
	}

}