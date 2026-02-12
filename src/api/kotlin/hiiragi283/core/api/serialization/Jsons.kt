package hiiragi283.core.api.serialization

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import hiiragi283.core.api.HTBuilderMarker

/**
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
@HTBuilderMarker
inline fun buildJson(builderAction: JsonObject.() -> Unit): JsonObject = JsonObject().apply(builderAction)

/**
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
@HTBuilderMarker
inline fun buildJsonArray(builderAction: JsonArray.() -> Unit): JsonArray = JsonArray().apply(builderAction)
