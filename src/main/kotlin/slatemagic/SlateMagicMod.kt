package slatemagic

import net.fabricmc.api.ModInitializer
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory
import slatemagic.block.SlateMagicBlocks
import slatemagic.block.entity.SlateMagicBlockEntities
import slatemagic.command.SlateMagicCommands
import slatemagic.command.type.SlateMagicArgumentTypes
import slatemagic.entity.SlateMagicEntities
import slatemagic.entity.tracked.SlateMagicTrackedData
import slatemagic.item.SlateMagicItems
import slatemagic.particle.SlateMagicParticles
import slatemagic.registry.SlateMagicRegistry
import slatemagic.spell.build.SlateMagicSpellNodes

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
		SlateMagicBlocks
		SlateMagicSpellNodes
		SlateMagicBlockEntities
		SlateMagicItems
		//	SlateMagicLoaders
	}

	/* TOOLS */
	fun id(id: String): Identifier = Identifier(MODID, id)

	fun i18n(type: String, id: String) = "$type.$MODID.$id"

	fun translatable(type: String, id: String) = Text.translatable(i18n(type, id))

	operator fun div(name: String) = id(name)

	fun error(msg: String) = logger.error(msg)
	fun warn(msg: String) = logger.warn(msg)
}