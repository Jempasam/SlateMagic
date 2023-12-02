package slabmagic.command.type

import com.mojang.brigadier.arguments.ArgumentType
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry
import net.minecraft.command.argument.serialize.ArgumentSerializer
import net.minecraft.command.argument.serialize.ArgumentSerializer.ArgumentTypeProperties
import slabmagic.SlabMagicMod

object SlabMagicArgumentTypes {

    private fun <T: ArgumentType<*>, P: ArgumentTypeProperties<T>>register(id: String, clazz: Class<T>, serializer: ArgumentSerializer<T,P>): ArgumentSerializer<T,P>{
        ArgumentTypeRegistry.registerArgumentType(SlabMagicMod.id(id), clazz, serializer)
        return serializer
    }

    val REGISTRY=register("registry", RegistryArgumentType::class.java, RegistryArgumentType.Serializer)
    val LIST=register("list", ListArgumentType::class.java, ListArgumentType.Serializer)
}