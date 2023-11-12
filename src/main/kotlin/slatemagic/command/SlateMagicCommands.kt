package slatemagic.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandException
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager.*
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.LiteralTextContent
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import slatemagic.command.commands.ParticleEffectCommand
import slatemagic.command.type.ListArgumentType
import slatemagic.command.type.RegistryArgumentType
import slatemagic.helper.ColorTools
import slatemagic.registry.SlateMagicRegistry
import slatemagic.shape.painter.GraphicsPainter
import slatemagic.spell.SpellContext
import slatemagic.spell.build.SpellNode
import slatemagic.spell.build.assemble
import slatemagic.spell.effect.SpellEffect
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.concurrent.thread
import slatemagic.spell.build.SPELL as SPELL_PART

object SlateMagicCommands {

    fun CommandContext<ServerCommandSource>.getSpell(name: String): SpellEffect {
        val spell = getArgument("spell", Object::class.java)
        if(spell is SpellEffect)return spell
        else if(spell is List<*>){
            val nodes=spell as List<SpellNode<*>>
            try{
                val result = assemble(nodes)
                if(result.type != SPELL_PART)throw CommandException(Text.of("Not a spell"))
                return SPELL_PART.get(result)
            }catch(e:Exception){
                throw CommandException(Text.of(e.message))
            }
        }
        else throw CommandException(Text.of("Expected spell, got ${spell}"))
    }

    private val CAST_SPELL=literal("cast").then(
        argument("power",IntegerArgumentType.integer(1)).executes { context ->
            val spell=context.getSpell("spell")
            val power=IntegerArgumentType.getInteger(context,"power")
            context.source.player?.also {
                val spellContext=SpellContext.atEye(it)
                spellContext.power=power
                spell.use(spellContext)
            }
            1
        }
    )

    private val PRINT_SPELL=literal("print").executes { context ->
        val spell=context.getSpell("spell")
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

    private val DESC_SPELL=literal("desc").executes { context ->
        val spell=context.getSpell("spell")
        val desc=spell.description.copy()
        desc.style= Style.EMPTY.withColor(ColorTools.int(spell.color))
        context.source.sendMessage(desc)
        1
    }

    private val NAME_SPELL=literal("name").executes { context ->
        val spell=context.getSpell("spell")
        val name=spell.name.copy()
        name.style= Style.EMPTY.withColor(ColorTools.int(spell.color))
        context.source.sendMessage(name)
        1
    }

    private val COST_SPELL=literal("cost").executes { context ->
        val spell=context.getSpell("spell")
        val costcount=spell.cost
        val cost= MutableText.of(LiteralTextContent("cost: $costcount"))
        cost.style= Style.EMPTY.withColor(ColorTools.int(spell.color))
        context.source.sendMessage(cost)
        costcount
    }

    fun ArgumentBuilder<ServerCommandSource, *>.spellNodes(): ArgumentBuilder<ServerCommandSource, *>{
        then(CAST_SPELL)
        then(PRINT_SPELL)
        then(DESC_SPELL)
        then(NAME_SPELL)
        return this
    }



    val SPELL= literal("spell").then(
        literal("spell")
            .then(argument("spell",RegistryArgumentType(SlateMagicRegistry.EFFECTS))
                .spellNodes()
            )
    ).then(
        literal("node")
            .then(argument("spell",ListArgumentType(RegistryArgumentType(SlateMagicRegistry.SPELL_NODES)))
                .spellNodes()
            )

    )

    val SPELL_NODE= literal("spell_node").then(argument("nodes",ListArgumentType(RegistryArgumentType(SlateMagicRegistry.SPELL_NODES)))
        .then(literal("try")
            .executes { context ->
                val nodes = ListArgumentType.get<SpellNode<*>>(context,"nodes")
                try{
                    val result = assemble(nodes)
                    val text = Text.of("${result.type.name}: ${result}")
                    context.source.sendMessage(text)
                    1
                }catch(e:Exception){
                    val text = Text.of(e.message)
                    context.source.sendMessage(text)
                    0
                }
            }
        )
    )

    init{
        CommandRegistrationCallback.EVENT.register{ dispatcher: CommandDispatcher<ServerCommandSource?>, registryAccess: CommandRegistryAccess?, environment: RegistrationEnvironment? ->
            dispatcher.register(SPELL)
            dispatcher.register(SPELL_NODE)
            dispatcher.register(ParticleEffectCommand.ROOT)
        }
    }
}