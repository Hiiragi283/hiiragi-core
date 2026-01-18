package hiiragi283.core.api.material

import hiiragi283.core.api.tag.HTTagPrefix

fun <V : Any> HTMaterialTable<HTTagPrefix, V>.getOrThrow(prefix: HTTagPrefix, material: HTMaterialLike): V =
    get(prefix, material) ?: error("Unknown ${prefix.name} for ${material.asMaterialName()}")
