package hiiragi283.core.api.serialization

import com.google.gson.JsonArray
import com.google.gson.JsonObject

/**
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
inline fun buildJson(builderAction: JsonObject.() -> Unit): JsonObject = JsonObject().apply(builderAction)

/**
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
inline fun buildJsonArray(builderAction: JsonArray.() -> Unit): JsonArray = JsonArray().apply(builderAction)
