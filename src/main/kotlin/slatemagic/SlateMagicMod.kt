package slatemagic

import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory
import slatemagic.command.SlateMagicCommands
import slatemagic.command.type.SlateMagicArgumentTypes
import slatemagic.entity.SlateMagicEntities
import slatemagic.entity.tracked.SlateMagicTrackedData
import slatemagic.particle.SlateMagicParticles
import slatemagic.registry.SlateMagicRegistry

object SlateMagicMod : ModInitializer {

	val MODID="slate-magic"

    private val logger = LoggerFactory.getLogger(MODID)

	override fun onInitialize() {
		logger.info("Initialization!")
		SlateMagicTrackedData
		SlateMagicEntities
		SlateMagicParticles
		SlateMagicRegistry
		SlateMagicArgumentTypes
		SlateMagicCommands

	}

	/* TOOLS */
	fun id(id: String): Identifier = Identifier(MODID, id)
}