package hiiragi283.core.internal.serialization.value

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.mojang.logging.LogUtils
import com.mojang.serialization.Decoder
import com.mojang.serialization.JsonOps
import hiiragi283.core.api.serialization.value.HTValueIOAccess
import hiiragi283.core.api.serialization.value.HTValueInput
import kotlin.jvm.optionals.getOrNull
import net.minecraft.core.HolderLookup
import net.minecraft.resources.RegistryOps
import org.slf4j.Logger

/**
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
internal class HTJsonValueInput(private val provider: HolderLookup.Provider, private val jsonObject: JsonObject) : HTValueInput {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    private val registryOps: RegistryOps<JsonElement> = provider.createSerializationContext(JsonOps.INSTANCE)

    private fun getJsonPrimitive(key: String): JsonPrimitive? {
        val jsonIn: JsonElement = jsonObject.get(key) ?: return null
        return jsonIn.takeIf(JsonElement::isJsonPrimitive)?.asJsonPrimitive
    }

    //    HTNbtInput    //

    override fun <T : Any> read(key: String, codec: Decoder<T>): T? {
        val jsonIn: JsonElement = jsonObject.get(key) ?: return null
        return codec.parse(registryOps, jsonIn).ifError { LOGGER.error(it.message()) }.result().getOrNull()
    }

    override fun child(key: String): HTValueInput? {
        val jsonIn: JsonObject = jsonObject.get(key) as? JsonObject ?: return null
        return HTValueIOAccess.createInput(provider, jsonIn)
    }

    override fun childOrEmpty(key: String): HTValueInput = child(key) ?: HTEmptyValueInput

    override fun childrenList(key: String): Iterable<HTValueInput>? {
        val list: JsonArray = jsonObject.get(key) as? JsonArray ?: return null
        return when {
            list.isEmpty -> null
            else -> list.filterIsInstance<JsonObject>().map { HTValueIOAccess.createInput(provider, it) }
        }
    }

    override fun childrenListOrEmpty(key: String): Iterable<HTValueInput> = childrenList(key) ?: emptySet()

    override fun <T : Any> list(key: String, codec: Decoder<T>): Iterable<T>? {
        val list: JsonArray = jsonObject.get(key) as? JsonArray ?: return null
        return when {
            list.isEmpty -> null
            else -> TypedInputList(provider, list, codec)
        }
    }

    override fun <T : Any> listOrEmpty(key: String, codec: Decoder<T>): Iterable<T> = list(key, codec) ?: emptySet()

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean = getJsonPrimitive(key)?.takeIf(JsonPrimitive::isBoolean)?.asBoolean ?: defaultValue

    override fun getByte(key: String, defaultValue: Byte): Byte = getJsonPrimitive(key)?.takeIf(JsonPrimitive::isNumber)?.asByte ?: defaultValue

    override fun getShort(key: String, defaultValue: Short): Short = getJsonPrimitive(key)?.takeIf(JsonPrimitive::isNumber)?.asShort ?: defaultValue

    override fun getInt(key: String): Int? = getJsonPrimitive(key)?.takeIf(JsonPrimitive::isNumber)?.asInt

    override fun getInt(key: String, defaultValue: Int): Int = getJsonPrimitive(key)?.takeIf(JsonPrimitive::isNumber)?.asInt ?: defaultValue

    override fun getLong(key: String): Long? = getJsonPrimitive(key)?.takeIf(JsonPrimitive::isNumber)?.asLong

    override fun getLong(key: String, defaultValue: Long): Long = getJsonPrimitive(key)?.takeIf(JsonPrimitive::isNumber)?.asLong ?: defaultValue

    override fun getFloat(key: String, defaultValue: Float): Float = getJsonPrimitive(key)?.takeIf(JsonPrimitive::isNumber)?.asFloat ?: defaultValue

    override fun getDouble(key: String, defaultValue: Double): Double = getJsonPrimitive(key)?.takeIf(JsonPrimitive::isNumber)?.asDouble ?: defaultValue

    override fun getString(key: String): String? = getJsonPrimitive(key)?.takeIf(JsonPrimitive::isString)?.asString

    override fun getString(key: String, defaultValue: String): String = getString(key) ?: defaultValue

    //    TypedInputList    //

    private class TypedInputList<T : Any>(provider: HolderLookup.Provider, private val list: JsonArray, private val codec: Decoder<T>) : Iterable<T> {
        private val registryOps: RegistryOps<JsonElement> = provider.createSerializationContext(JsonOps.INSTANCE)

        override fun iterator(): Iterator<T> = list
            .mapNotNull { json: JsonElement ->
                codec.parse(registryOps, json).ifError { LOGGER.error(it.message()) }.result().getOrNull()
            }.iterator()
    }
}
