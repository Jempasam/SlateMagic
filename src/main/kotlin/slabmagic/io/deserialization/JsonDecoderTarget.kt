package slabmagic.io.deserialization

import com.google.gson.JsonElement
import com.google.gson.JsonObject

class JsonDecoderTarget(val obj: JsonObject): DecoderTarget<JsonElement> {

    override fun asString(value: JsonElement): String = value.asString
    override fun asInt(value: JsonElement) = value.asInt
    override fun asFloat(value: JsonElement) = value.asFloat
    override fun asDouble(value: JsonElement) = value.asDouble
    override fun asBoolean(value: JsonElement) = value.asBoolean
    override fun asSub(value: JsonElement) = JsonDecoderTarget(value.asJsonObject)

    override fun get(str: String): JsonElement = obj.get(str) ?: throw IllegalArgumentException("Missing $str")
    override fun getOpt(str: String): JsonElement? = obj.get(str)

    override fun <T> getMany(str: String, converter: (JsonElement) -> T): List<T> {
        return obj.getAsJsonArray(str).map { converter(it) }
    }
}