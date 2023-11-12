package slatemagic.command

import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess
import slatemagic.SlateMagicModClient
import slatemagic.command.type.RegistryArgumentType
import slatemagic.registry.SlateMagicRegistry
import slatemagic.spell.effect.PrecalculatedSpellEffect
import slatemagic.spell.effect.SpellEffect


object SlateMagicClientCommands {

    val SPELL= literal("spellcli").then(
        argument("spell",RegistryArgumentType(SlateMagicRegistry.EFFECTS)).then(
            literal("show").executes { context ->
                val spell=context.getArgument("spell", SpellEffect::class.java)
                SlateMagicModClient.spell = PrecalculatedSpellEffect(spell)
                SlateMagicModClient.lastTime = System.currentTimeMillis()
                1
            }
        )
    )

    init{
        ClientCommandRegistrationCallback.EVENT.register{ dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess ->
            dispatcher.register(SPELL)
        }
    }
}