package slabmagic.io.loader

import com.google.common.base.Predicates
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraft.util.profiler.Profiler
import slabmagic.SlabMagicMod
import java.io.InputStreamReader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

abstract class JsonLoader<T>(val id: Identifier, val dir: String): SimpleResourceReloadListener<List<Pair<Identifier,T>>> {

    override fun load(manager: ResourceManager, profiler: Profiler, executor: Executor): CompletableFuture<List<Pair<Identifier,T>>> {
        return CompletableFuture.supplyAsync{
            val resources=manager.findResources("$dir/", Predicates.alwaysTrue())
            val ret= mutableListOf<Pair<Identifier,T>>()
            for(res in resources){
                try{
                    val obj=JsonHelper.deserialize(InputStreamReader(res.value.inputStream))
                    ret.add(res.key to deserialize(obj))
                }catch (e: Exception){
                    SlabMagicMod.warn("For ressource \"$id\" in \"$dir\", in file \"${res.key}\": "+e.message);
                }
            }
            ret
        }
    }

    override fun apply(data: List<Pair<Identifier,T>>, manager: ResourceManager, profiler: Profiler, executor: Executor): CompletableFuture<Void> {
        return CompletableFuture.runAsync{apply(data,manager,profiler)}
    }

    @Throws(JsonParseException::class)
    abstract fun deserialize(json: JsonElement): T

    abstract fun apply(data: List<Pair<Identifier,T>>, manager: ResourceManager, profiler: Profiler)

    override fun getFabricId(): Identifier = id

    companion object{
        inline fun <T> enter(zone: String, action: ()->T): T{
            try{
                return action()
            }
            catch (e: Exception){
                throw JsonParseException("In \"$zone\": "+e.message)
            }
        }
    }
}