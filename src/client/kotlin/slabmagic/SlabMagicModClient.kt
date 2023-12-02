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
	}

}