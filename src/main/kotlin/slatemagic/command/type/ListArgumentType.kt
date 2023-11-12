package slatemagic.command.type

import com.google.common.collect.Iterables
import com.google.gson.JsonObject
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.fabricmc.fabric.mixin.command.ArgumentTypesAccessor
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.argument.serialize.ArgumentSerializer
import net.minecraft.command.argument.serialize.ArgumentSerializer.ArgumentTypeProperties
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.concurrent.CompletableFuture
import kotlin.math.max

class ListArgumentType<T, A: ArgumentType<T>>(val type: A): ArgumentType<List<T>> {

    companion object{
        fun <T> get(context: CommandContext<*>, name: String): List<T> {
            return context.getArgument(name,List::class.java) as List<T>
        }
    }

    @Throws(CommandSyntaxException::class)
    override fun parse(stringReader: StringReader): List<T> {
        stringReader.expect('[')
        val list = mutableListOf<T>()
        while(stringReader.canRead() && stringReader.peek() != ']') {
            list.add(type.parse(stringReader))
            if(stringReader.canRead() && stringReader.peek()==']'){
                stringReader.read()
                break
            }
            stringReader.expect(',')
        }
        return list
    }

    override fun getExamples(): Collection<String> {
        val subExample=Iterables.cycle(type.examples).iterator()
        return listOf(
            "["+subExample.next()+" "+subExample+"]",
            "["+subExample.next()+" ]"
        )
    }

    override fun <S> listSuggestions( context: CommandContext<S>, builder: SuggestionsBuilder ): CompletableFuture<Suggestions> {
        val remaining=builder.remaining
        if(remaining.length==0){
            builder.suggest("[")
            return builder.buildFuture()
        }
        else{
            val lastStart= max(max(remaining.lastIndexOf('['),remaining.lastIndexOf(',')),0)+1
            val subbuilder=builder.createOffset(builder.start+lastStart)
            return if(lastStart==remaining.length){
                subbuilder.suggest("]")
                subbuilder.suggest(",")
                println(subbuilder.remaining)
                type.listSuggestions(context,subbuilder)
            }
            else{
                type.listSuggestions(context,subbuilder)
            }
        }
    }

    class Properties(val propertis: ArgumentTypeProperties<*>) : ArgumentTypeProperties<ListArgumentType<*,*>> {
        override fun createType(commandRegistryAccess: CommandRegistryAccess): ListArgumentType<*,*> {
            return create(propertis.createType(commandRegistryAccess) as ArgumentType<*>)
        }

        private fun <T,A: ArgumentType<T>>create(type: A): ListArgumentType<T,A> {
            return ListArgumentType(type)
        }

        override fun getSerializer(): ArgumentSerializer<ListArgumentType<*,*>, Properties> {
            return Serializer
        }
    }


    object Serializer : ArgumentSerializer<ListArgumentType<*,*>, Properties> {
        override fun writePacket(properties: Properties, buf: PacketByteBuf) {
            write(properties.propertis, buf)
        }

        fun <A: ArgumentType<*>, P: ArgumentTypeProperties<A>> write(properties: P, buf: PacketByteBuf){
            val serializer=properties.serializer as ArgumentSerializer<A,P>
            val id=Registry.COMMAND_ARGUMENT_TYPE.getId(serializer)
            buf.writeString(id.toString())
            serializer.writePacket(properties,buf)
        }

        override fun fromPacket(packetByteBuf: PacketByteBuf): Properties {
            val id=Identifier(packetByteBuf.readString())
            val serializer=Registry.COMMAND_ARGUMENT_TYPE.get(id)
            return Properties(serializer?.fromPacket(packetByteBuf) as ArgumentTypeProperties<*>)
        }

        override fun writeJson(properties: Properties, jsonObject: JsonObject) {
            write(properties.propertis, jsonObject)
        }

        fun <A: ArgumentType<*>, P: ArgumentTypeProperties<A>> write(properties: P, jsonObject: JsonObject){
            val serializer=properties.serializer as ArgumentSerializer<A,P>
            val id=Registry.COMMAND_ARGUMENT_TYPE.getId(serializer)
            jsonObject.addProperty("type",id.toString())
            val obj=JsonObject()
            serializer.writeJson(properties,obj)
            jsonObject.add("value",obj)
        }

        override fun getArgumentTypeProperties(entityArgumentType: ListArgumentType<*,*>): Properties {
            val type=entityArgumentType.type
            return getProp(type)
        }

        fun <T,A:ArgumentType<T>> getProp(type: A): Properties {
            val serializer=ArgumentTypesAccessor.fabric_getClassMap().get(type.javaClass) as ArgumentSerializer<A,*>
            return Properties(serializer.getArgumentTypeProperties(type))
        }
    }


}
