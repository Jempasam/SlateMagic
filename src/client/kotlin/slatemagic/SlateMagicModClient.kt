package slatemagic

import assets.`slate-magic`.block.SlateMagicBlockColors
import assets.`slate-magic`.block.SlateMagicItemColors
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import slatemagic.command.SlateMagicClientCommands
import slatemagic.entity.SlateMagicRenderers
import slatemagic.network.SlateMagicClientNetwork
import slatemagic.particle.SlateMagicClientParticles
import slatemagic.spell.effect.SpellEffect
import slatemagic.ui.ShowSpellGUI

object SlateMagicModClient : ClientModInitializer {

	var spell: SpellEffect?=null
	var lastTime: Long=0

	override fun onInitializeClient() {
		SlateMagicClientParticles
		SlateMagicClientCommands
		SlateMagicClientParticles
		SlateMagicClientNetwork
		SlateMagicRenderers
		SlateMagicBlockColors
		SlateMagicItemColors
		HudRenderCallback.EVENT.register(ShowSpellGUI())
	}

}