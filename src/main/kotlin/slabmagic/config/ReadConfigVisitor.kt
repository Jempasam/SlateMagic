package slabmagic.config

import com.google.gson.JsonElement
import com.google.gson.JsonObject

class ReadConfigVisitor(val obj: JsonObject): ConfigVisitor {
    override fun <T : JsonElement> visit(name: String, type: Class<out T>, def: () -> T, setter: (T) -> Unit) {
        try{
            val value = obj.get(name)
            if(value != null){
                val typedValue = type.cast(value)
                setter(typedValue)
            }
        }catch (e: Exception){
            println("Error reading config value $name: ${e.message}")
        }
    }
}