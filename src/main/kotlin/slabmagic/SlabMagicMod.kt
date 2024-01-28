package slabmagic

import net.fabricmc.api.ModInitializer
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory
import slabmagic.block.SlabMagicBlocks
import slabmagic.block.entity.SlabMagicBlockEntities
import slabmagic.command.SlabMagicCommands
import slabmagic.command.type.SlabMagicArgumentTypes
import slabmagic.entity.SlabMagicEntities
import slabmagic.entity.tracked.SlabMagicTrackedData
import slabmagic.item.SlabMagicItems
import slabmagic.particle.SlabMagicParticles
import slabmagic.registry.SlabMagicRegistry
import slabmagic.spell.build.parts.SlabMagicSpellParts

object SlabMagicMod : ModInitializer {

	const val MODID="slab-magic"

    private val logger = LoggerFactory.getLogger(MODID)

	override fun onInitialize() {
		logger.info("Initialization!")
		SlabMagicTrackedData
		SlabMagicEntities
		SlabMagicParticles
		SlabMagicRegistry
		SlabMagicArgumentTypes
		SlabMagicCommands
		SlabMagicBlocks
		SlabMagicSpellParts
		SlabMagicBlockEntities
		SlabMagicItems
	}

	/* TOOLS */
	fun id(id: String): Identifier = Identifier(MODID, id)

	fun i18n(type: String, id: String) = "$type.$MODID.$id"

	fun translatable(type: String, id: String) = Text.translatable(i18n(type, id))

	operator fun div(name: String) = id(name)

	fun error(msg: String) = logger.error(msg)
	fun warn(msg: String) = logger.warn(msg)
}