package slatemagic.io.loader

import com.google.gson.JsonElement
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraft.util.profiler.Profiler
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import slatemagic.SlateMagicMod
import slatemagic.io.deserialization.JsonDecoderTarget
import slatemagic.registry.SlateMagicRegistry
import slatemagic.spell.build.SpellNode

object SpellNodeLoader: JsonLoader<SpellNode<*>>(SlateMagicMod.id("spell_node_loader"),"spell_nodes") {
    override fun deserialize(json: JsonElement): SpellNode<*> {
        val obj=JsonHelper.asObject(json,"root")
        val type=JsonHelper.getString(obj,"type")
        val decoder = enter("type"){
            SlateMagicRegistry.NODE_DECODERS.getOrThrow(RegistryKey.of(SlateMagicRegistry.NODE_DECODERS.key, Identifier(type)))
        }
        return decoder.decode(JsonDecoderTarget(obj))
    }

    override fun apply(data: List<Pair<Identifier,SpellNode<*>>>, manager: ResourceManager, profiler: Profiler) {
        for(node in data){
            Registry.register(SlateMagicRegistry.NODES, node.first, node.second)
        }
    }
}