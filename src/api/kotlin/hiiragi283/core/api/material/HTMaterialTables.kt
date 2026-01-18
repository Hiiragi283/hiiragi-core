package hiiragi283.core.api.material

import hiiragi283.core.api.collection.ImmutableTable
import hiiragi283.core.api.tag.HTTagPrefix

fun <V : Any> ImmutableTable<HTTagPrefix, HTMaterialKey, V>.getOrThrow(prefix: HTTagPrefix, key: HTMaterialKey): V =
    get(prefix, key) ?: error("Unknown ${prefix.name} for ${key.asMaterialId()}")
