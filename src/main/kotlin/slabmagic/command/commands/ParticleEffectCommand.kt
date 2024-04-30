package slabmagic.command.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.command.argument.ParticleEffectArgumentType
import net.minecraft.command.argument.Vec3ArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import slabmagic.network.ServerNetwork
import slabmagic.network.messages.AdvancedParticleMessage
import slabmagic.network.messages.AdvancedParticleMessage.Shape.*

object ParticleEffectCommand: CommandRegistrationCallback {

    override fun register(disp: CommandDispatcher<ServerCommandSource>, registry: CommandRegistryAccess, env: CommandManager.RegistrationEnvironment) {
        val ROOT= CommandManager.literal("particleeffect").then(
            CommandManager.argument("effect", ParticleEffectArgumentType.particleEffect(registry)).then(
                CommandManager.argument("position", Vec3ArgumentType.vec3()).then(
                    CommandManager.argument("velocity", Vec3ArgumentType.vec3(false)).then(
                        CommandManager.argument("viewer", EntityArgumentType.players()).then(

                            // LINE
                            sendParticle( "line", LINE, "to", "spreading", true)
                        ).then(
                            sendParticle( "curve", CURVE, "to", "curvature", true)
                        ).then(
                            sendParticle("lightning", LIGHTNING, "to", "bend count", true)
                        ).then(

                            // BOX
                            sendParticle("box", BOX, "size", "count", false)
                        ).then(
                            sendParticle("cloud", CLOUD, "size", "count", false)
                        ).then(

                            // BOOM AND MOTION ZONE
                            sendParticle("boom", BOOM, "speed", "count", false)
                        ).then(
                            sendParticle("cloudboom", CLOUD_BOOM, "speed", "count", false)
                        ).then(
                            sendParticle("implode", IMPLODE, "size", "count", false)
                        ).then(
                            sendParticle("spiral", SPIRAL, "size", "count", false)
                        ).then(
                            sendParticle("storm", STORM, "size and speed", "count", false)
                        ).then(

                            // RINGS
                            sendParticle("ring", RING, "size", "count", false)
                        ).then(
                            sendParticle("shockwave", SHOCKWAVE, "size", "count", false)
                        ).then(
                            sendParticle("tornado", TORNADO, "size", "count", false)
                        )
                    )
                )
            )
        )
    }

    private fun sendParticle(name: String, shape: AdvancedParticleMessage.Shape, toName: String, countName: String, centerInteger: Boolean): LiteralArgumentBuilder<ServerCommandSource> {
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
                        viewers,
                        AdvancedParticleMessage(
                            effect,
                            shape,
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