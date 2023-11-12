package slatemagic.command.type

import com.mojang.brigadier.arguments.ArgumentType
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry
import net.minecraft.command.argument.serialize.ArgumentSerializer
import net.minecraft.command.argument.serialize.ArgumentSerializer.ArgumentTypeProperties
import slatemagic.SlateMagicMod

object SlateMagicArgumentTypes {

    private fun <T: ArgumentType<*>, P: ArgumentTypeProperties<T>>register(id: String, clazz: Class<T>, serializer: ArgumentSerializer<T,P>): ArgumentSerializer<T,P>{
        ArgumentTypeRegistry.registerArgumentType(SlateMagicMod.id(id), clazz, serializer)
        return serializer
    }

    val REGISTRY=register("registry", RegistryArgumentType::class.java, RegistryArgumentType.Serializer)
}