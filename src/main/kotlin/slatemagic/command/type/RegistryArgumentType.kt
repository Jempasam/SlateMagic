package slatemagic.command.type

import com.google.gson.JsonObject
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.CommandSource
import net.minecraft.command.argument.serialize.ArgumentSerializer
import net.minecraft.command.argument.serialize.ArgumentSerializer.ArgumentTypeProperties
import net.minecraft.network.PacketByteBuf
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.registry.BuiltinRegistries
import net.minecraft.util.registry.Registry
import slatemagic.registry.SlateMagicRegistry
import java.util.concurrent.CompletableFuture

class RegistryArgumentType<T>(val registry: Registry<T>): ArgumentType<T> {

    @Throws(CommandSyntaxException::class)
    override fun parse(stringReader: StringReader): T {
        val identifier=Identifier.fromCommandInput(stringReader)
        val obj= registry.get(identifier) ?: throw DOES_NOT_EXIST.create(identifier)
        return obj
    }

    override fun getExamples(): Collection<String> {
        return EXAMPLES
    }

    override fun <S> listSuggestions( context: CommandContext<S>, builder: SuggestionsBuilder ): CompletableFuture<Suggestions> {
        return CommandSource.suggestIdentifiers(registry.ids, builder);
    }

    companion object {

        private val EXAMPLES: Collection<String> = mutableListOf("slate-magic:fireball", "slate-magic:iceball")

        val DOES_NOT_EXIST = DynamicCommandExceptionType({Text.translatable("commands.hexlink.registry.unexisting",it)})

    }


    class Properties(val registry: Registry<*>) : ArgumentTypeProperties<RegistryArgumentType<*>> {
        override fun createType(commandRegistryAccess: CommandRegistryAccess): RegistryArgumentType<*> {
            return create(registry)
        }

        private fun <T>create(registry: Registry<T>): RegistryArgumentType<T> {
            return RegistryArgumentType(registry)
        }

        override fun getSerializer(): ArgumentSerializer<RegistryArgumentType<*>, Properties> {
            return Serializer
        }
    }


    object Serializer : ArgumentSerializer<RegistryArgumentType<*>, Properties> {
        override fun writePacket(properties: Properties, packetByteBuf: PacketByteBuf) {
            packetByteBuf.writeString(properties.registry.key.value.toString())
        }

        override fun fromPacket(packetByteBuf: PacketByteBuf): Properties {
            val id=Identifier(packetByteBuf.readString(32767))
            return Properties(getRegistry(id))
        }

        override fun writeJson(properties: Properties, jsonObject: JsonObject) {
            jsonObject.addProperty("registry", properties.registry.key.value.toString())
        }

        override fun getArgumentTypeProperties(entityArgumentType: RegistryArgumentType<*>): Properties {
            return Properties(entityArgumentType.registry)
        }

        fun getRegistry(id: Identifier): Registry<*>{
            return Registry.REGISTRIES.get(id)
                ?: BuiltinRegistries.REGISTRIES.get(id)
                ?: SlateMagicRegistry.DYNAMICS.get(id)
                ?: Registry.ENCHANTMENT
        }
    }


}