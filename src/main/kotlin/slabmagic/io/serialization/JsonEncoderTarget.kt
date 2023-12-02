package slabmagic.io.serialization

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

class JsonEncoderTarget(val json: JsonObject): EncoderTarget<JsonElement> {
    override fun create(str: String) = JsonPrimitive(str)

    override fun create(int: Int) = JsonPrimitive(int)

    override fun create(float: Float) = JsonPrimitive(float)

    override fun create(double: Double) = JsonPrimitive(double)

    override fun create(boolean: Boolean) = JsonPrimitive(boolean)

    override fun createSub(): EncoderTarget<JsonElement> {
        return JsonEncoderTarget(JsonObject())
    }



    override fun <T> putMany(key: String, value: List<T>, converter: (T) -> JsonElement) {
        val array=JsonArray()
        value.forEach { array.add(converter(it)) }
        json.add(key, array)
    }

    override fun putOpt(key: String, value: JsonElement?) {
        if(value != null) json.add(key, value)
    }

    override fun put(key: String, value: JsonElement) {
        json.add(key, value)
    }


    override fun asValue(): JsonElement {
        return json
    }
}