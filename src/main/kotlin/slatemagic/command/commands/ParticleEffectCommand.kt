package slatemagic.command.commands

import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.command.argument.ParticleEffectArgumentType
import net.minecraft.command.argument.Vec3ArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.Identifier
import slatemagic.network.ServerNetwork
import slatemagic.network.messages.AdvancedParticleMessage

object ParticleEffectCommand {


    val ROOT= CommandManager.literal("particleeffect").then(
        CommandManager.argument("effect", ParticleEffectArgumentType.particleEffect()).then(
            CommandManager.argument("position", Vec3ArgumentType.vec3()).then(
                CommandManager.argument("velocity", Vec3ArgumentType.vec3(false)).then(
                    CommandManager.argument("viewer", EntityArgumentType.players()).then(

                        // LINE
                        sendParticle( "line", AdvancedParticleMessage.LINE, "to", "spreading", true)
                    ).then(
                        sendParticle( "curve", AdvancedParticleMessage.CURVE, "to", "curvature", true)
                    ).then(
                        sendParticle("lightning", AdvancedParticleMessage.LIGHTNING, "to", "bend count", true)
                    ).then(

                        // BOX
                        sendParticle("box", AdvancedParticleMessage.BOX, "size", "count", false)
                    ).then(
                        sendParticle("cloud", AdvancedParticleMessage.CLOUD, "size", "count", false)
                    ).then(

                        // BOOM AND MOTION ZONE
                        sendParticle("boom", AdvancedParticleMessage.BOOM, "speed", "count", false)
                    ).then(
                        sendParticle("cloudboom", AdvancedParticleMessage.CLOUD_BOOM, "speed", "count", false)
                    ).then(
                        sendParticle("implode", AdvancedParticleMessage.IMPLODE, "size", "count", false)
                    ).then(
                        sendParticle("spiral", AdvancedParticleMessage.SPIRAL, "size", "count", false)
                    ).then(
                        sendParticle("storm", AdvancedParticleMessage.STORM, "size and speed", "count", false)
                    ).then(

                        // RINGS
                        sendParticle("ring", AdvancedParticleMessage.RING, "size", "count", false)
                    ).then(
                        sendParticle("shockwave", AdvancedParticleMessage.SHOCKWAVE, "size", "count", false)
                    ).then(
                        sendParticle("tornado", AdvancedParticleMessage.TORNADO, "size", "count", false)
                    )
                )
            )
        )
    )


    private fun sendParticle(name: String, type: Identifier, toName: String, countName: String, centerInteger: Boolean): LiteralArgumentBuilder<ServerCommandSource> {
        return CommandManager.literal(name).then(
            CommandManager.argument(toName, Vec3ArgumentType.vec3(centerInteger)).then(
                CommandManager.argument(countName, DoubleArgumentType.doubleArg(0.0)).executes { context ->
                    val effect= ParticleEffectArgumentType.getParticle(context, "effect")
                    val from= Vec3ArgumentType.getVec3(context, "position")
                    val to= Vec3ArgumentType.getVec3(context, toName)
                    val speed= Vec3ArgumentType.getVec3(context, "velocity")
                    val spreading= DoubleArgumentType.getDouble(context, countName)
                    val viewers= EntityArgumentType.getPlayers(context, "viewer")
                    ServerNetwork.sendToAll(
                        viewers, type,
                        AdvancedParticleMessage(
                            effect,
                            context.source.world.registryKey,
                            from, to,
                            speed, spreading
                        )
                    )
                    1
                }
            )
        )
    }


}