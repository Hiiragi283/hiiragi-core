package hiiragi283.core.api.serialization

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import hiiragi283.core.api.HTBuilderMarker

@HTBuilderMarker
inline fun buildJson(builderAction: JsonObject.() -> Unit): JsonObject = JsonObject().apply(builderAction)

@HTBuilderMarker
inline fun buildJsonArray(builderAction: JsonArray.() -> Unit): JsonArray = JsonArray().apply(builderAction)
