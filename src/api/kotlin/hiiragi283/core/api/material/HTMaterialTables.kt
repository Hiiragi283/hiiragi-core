package hiiragi283.core.api.material

import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike

operator fun <V : Any> HTMaterialTable<HTMaterialPrefix, V>.get(prefix: HTPrefixLike, material: HTMaterialLike): V? =
    this[prefix.asMaterialPrefix(), material.asMaterialKey()]

fun <V : Any> HTMaterialTable<HTMaterialPrefix, V>.getOrThrow(prefix: HTPrefixLike, material: HTMaterialLike): V =
    get(prefix, material) ?: error("Unknown ${prefix.asPrefixName()} for ${material.asMaterialName()}")

fun <V : Any> HTMaterialTable<HTMaterialPrefix, V>.row(prefix: HTPrefixLike): Map<HTMaterialKey, V> = this.row(prefix.asMaterialPrefix())
