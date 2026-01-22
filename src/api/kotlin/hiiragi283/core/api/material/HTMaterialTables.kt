package hiiragi283.core.api.material

import hiiragi283.core.api.collection.HTTable
import hiiragi283.core.api.tag.HTTagPrefix

fun <V : Any> HTTable<HTTagPrefix, HTMaterialKey, V>.getOrThrow(prefix: HTTagPrefix, key: HTMaterialKey): V =
    get(prefix, key) ?: error("Unknown ${prefix.name} for ${key.asMaterialId()}")
