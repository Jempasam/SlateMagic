package slatemagic.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager.*
import net.minecraft.server.command.ServerCommandSource
import slatemagic.command.commands.ParticleEffectCommand
import slatemagic.command.type.RegistryArgumentType
import slatemagic.helper.ColorTools
import slatemagic.registry.SlateMagicRegistry
import slatemagic.shape.painter.GraphicsPainter
import slatemagic.spell.Spell
import slatemagic.spell.SpellContext
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.concurrent.thread


object SlateMagicCommands {

    val SPELL= literal("spell").then(
        argument("spell",RegistryArgumentType(SlateMagicRegistry.SPELLS)).then(
            literal("cast").then(
                argument("power",IntegerArgumentType.integer(1)).executes { context ->
                    val spell=context.getArgument("spell",Spell::class.java)
                    val power=IntegerArgumentType.getInteger(context,"power")
                    context.source.player?.also {
                        val spell_context=SpellContext.atEye(it)
                        spell_context.power=power
                        spell.use(spell_context)
                    }
                    1
                }
            )
        ).then(
            literal("print").executes { context ->
                val spell=context.getArgument("spell",Spell::class.java)
                val image= BufferedImage(500,500, BufferedImage.TYPE_INT_ARGB)
                val g=image.graphics
                val painter=GraphicsPainter(g,ColorTools.awt(spell.color), 0,0,500,500)
                spell.shape.draw(painter)
                g.dispose()

                ImageIO.write(image,"png",File("spell_${spell.name.string}.png"))
                thread(start=true){

                }
                1
            }
        )
    )

    init{
        CommandRegistrationCallback.EVENT.register{ dispatcher: CommandDispatcher<ServerCommandSource?>, registryAccess: CommandRegistryAccess?, environment: RegistrationEnvironment? ->
            dispatcher.register(SPELL)
            dispatcher.register(ParticleEffectCommand.ROOT)
        }
    }
}