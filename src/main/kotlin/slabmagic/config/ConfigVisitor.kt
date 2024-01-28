package slabmagic.config

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

interface ConfigVisitor {
    fun <T: JsonElement> visit(name: String, type: Class<out T>, def: ()->T, setter: (T)->Unit)

    fun primitive(name: String, def: ()->String, setter: (JsonPrimitive)->Unit)
        = visit(name, JsonPrimitive::class.java, { JsonPrimitive(def()) }, setter)

    fun array(name: String, setter: (JsonArray)->Unit)
        = visit(name, JsonArray::class.java, {JsonArray()}, setter)

    fun obj(name: String, setter: (JsonObject)->Unit)
        = visit(name, JsonObject::class.java, {JsonObject()}, setter)

}