package slabmagic.command

import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess
import slabmagic.SlabMagicModClient
import slabmagic.command.type.RegistryArgumentType
import slabmagic.registry.SlabMagicRegistry
import slabmagic.spell.effect.PrecalculatedSpellEffect
import slabmagic.spell.effect.SpellEffect


object SlabMagicClientCommands {

    val SPELL= literal("spellcli").then(
        argument("spell",RegistryArgumentType(SlabMagicRegistry.EFFECTS)).then(
            literal("show").executes { context ->
                val spell=context.getArgument("spell", SpellEffect::class.java)
                SlabMagicModClient.spell = PrecalculatedSpellEffect(spell)
                SlabMagicModClient.lastTime = System.currentTimeMillis()
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