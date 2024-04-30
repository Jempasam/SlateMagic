package slabmagic.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.argument.BlockPosArgumentType
import net.minecraft.command.argument.PosArgument
import net.minecraft.server.command.CommandManager.*
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.math.Direction
import slabmagic.SlabMagicMod
import slabmagic.block.properties.visitAt
import slabmagic.command.commands.ParticleEffectCommand
import slabmagic.command.type.ListArgumentType
import slabmagic.command.type.RegistryArgumentType
import slabmagic.helper.ColorTools
import slabmagic.particle.SlabMagicParticles
import slabmagic.particle.SpellCircleParticleEffect
import slabmagic.registry.SlabMagicRegistry
import slabmagic.shape.painter.GraphicsPainter
import slabmagic.spell.SpellContext
import slabmagic.spell.build.AssembledSpell
import slabmagic.spell.build.assembleSpell
import slabmagic.spell.build.parts.SpellPart
import slabmagic.spell.build.visitAll
import slabmagic.spell.build.visitor.AssemblingPartVisitor
import slabmagic.spell.effect.SpellEffect
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.concurrent.thread
import slabmagic.spell.build.parts.SPELL as SPELL_PART

object SlabMagicCommands: CommandRegistrationCallback {
    override fun register(disp: CommandDispatcher<ServerCommandSource>, registry: CommandRegistryAccess, env: RegistrationEnvironment) {

        // Exceptions
        val INVALID_NODE_ASSEMBLING=SimpleCommandExceptionType(Text.of("Invalid node assembling"))

        val NOT_A_SPELL=SimpleCommandExceptionType(Text.of("Not a spell"))

        val EXPECTED_SPELL=DynamicCommandExceptionType{arg->Text.of("Expected spell, got $arg")}

        val UNKNOWN_EXCEPTION=DynamicCommandExceptionType{arg->Text.of("$arg")}

        // Commands
        fun CommandContext<ServerCommandSource>.getSpell(name: String): SpellEffect {
            when (val arg=getArgument("spell", Any::class.java)) {
                is AssembledSpell -> return arg.effect

                is List<*> -> {
                    val nodes=arg as List<SpellPart<*>>
                    try{
                        return nodes.assembleSpell()?.effect ?: throw NOT_A_SPELL.create()
                    }catch(e:Exception){
                        throw UNKNOWN_EXCEPTION.create(e.message)
                    }
                }

                is PosArgument -> {
                    try{
                        val blockpos=arg.toAbsoluteBlockPos(source)
                        val visitor= AssemblingPartVisitor()
                        visitor.visitAt(source.world,blockpos,Direction.UP)
                        return visitor.result?.first?.let(SPELL_PART::cast)?.effect
                            ?: throw NOT_A_SPELL.create()
                    }
                    catch (e: CommandSyntaxException){ throw e }
                    catch (e: Throwable){ throw UNKNOWN_EXCEPTION.create(e.message) }
                }

                else ->{
                    throw EXPECTED_SPELL.create(arg)
                }
            }
        }

        val CAST_SPELL=literal("cast").then(
            argument("power",IntegerArgumentType.integer(1)).executes { context ->
                val spell=context.getSpell("spell")
                val power=IntegerArgumentType.getInteger(context,"power")
                context.source.player?.also {
                    val spellContext=SpellContext.atEye(it, SpellContext.Stored(power))
                    spell.use(spellContext)
                }
                1
            }
        )

        val PRINT_SPELL=literal("print").executes { context ->
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

        val DESC_SPELL=literal("desc").executes { context ->
            val spell=context.getSpell("spell")
            val desc=spell.description.copy()
            desc.style= Style.EMPTY.withColor(ColorTools.int(spell.color))
            context.source.sendMessage(desc)
            1
        }

        val SHOW_SPELL=literal("show").executes { context ->
            val spell=context.getSpell("spell")
            context.source.world.spawnParticles(
                SpellCircleParticleEffect(
                    SlabMagicParticles.SPELL_CIRCLE,
                    spell.shape,
                    ColorTools.int(spell.color),
                    2f, 200
                ),
                context.source.position.x, context.source.position.y, context.source.position.z,
                1,
                0.0, 0.0, 0.0,
                0.0
            )
            1
        }

        val NAME_SPELL=literal("name").executes { context ->
            val spell=context.getSpell("spell")
            val name=spell.name.copy()
            name.style= Style.EMPTY.withColor(ColorTools.int(spell.color))
            context.source.sendMessage(name)
            1
        }

        val COST_SPELL=literal("cost").executes { context ->
            val spell=context.getSpell("spell")
            val costcount=spell.cost
            val cost= Text.literal("cost: $costcount")
            cost.style= Style.EMPTY.withColor(ColorTools.int(spell.color))
            context.source.sendMessage(cost)
            costcount
        }

        fun ArgumentBuilder<ServerCommandSource, *>.spellNodes(): ArgumentBuilder<ServerCommandSource, *>{
            then(CAST_SPELL)
            then(PRINT_SPELL)
            then(DESC_SPELL)
            then(NAME_SPELL)
            then(SHOW_SPELL)
            return this
        }



        val SPELL= literal("spell").then(
            literal("spell")
                .then(argument("spell",RegistryArgumentType(SlabMagicRegistry.EFFECTS,SlabMagicMod.MODID))
                    .spellNodes()
                )
        ).then(
            literal("node")
                .then(argument("spell",ListArgumentType(RegistryArgumentType(SlabMagicRegistry.PARTS,SlabMagicMod.MODID)))
                    .spellNodes()
                )

        ).then(
            literal("slab")
                .then(argument("spell",BlockPosArgumentType.blockPos())
                    .spellNodes()
                )
        )

        val SPELL_NODE= literal("spell_node").then(argument("nodes",ListArgumentType(RegistryArgumentType(SlabMagicRegistry.PARTS)))
            .then(literal("try")
                .executes { context ->
                    val nodes = ListArgumentType.get<SpellPart<*>>(context,"nodes")
                    try{
                        val visitor=AssemblingPartVisitor()
                        visitor.visitAll(nodes)
                        val result=visitor.result?.first ?: throw INVALID_NODE_ASSEMBLING.create()
                        val text = Text.of("${result.type.name}: $result")
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

        disp.register(SPELL)
        disp.register(SPELL_NODE)
    }

    init{
        CommandRegistrationCallback.EVENT.register(this)
        CommandRegistrationCallback.EVENT.register(ParticleEffectCommand)
    }
}