package hiiragi283.core.api.material.prefix

import hiiragi283.core.api.collection.HTTable
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike

@JvmInline
value class HTMaterialTable<V : Any>(private val table: HTTable<HTMaterialPrefix, HTMaterialKey, V>) :
    HTTable<HTMaterialPrefix, HTMaterialKey, V> by table {
    operator fun get(prefix: HTPrefixLike, material: HTMaterialLike): V? = table[prefix.asMaterialPrefix(), material.asMaterialKey()]

    fun getOrThrow(prefix: HTPrefixLike, material: HTMaterialLike): V =
        get(prefix, material) ?: error("Unknown ${prefix.asPrefixName()} for ${material.asMaterialName()}")

    fun row(prefix: HTPrefixLike): Map<HTMaterialKey, V> = table.row(prefix.asMaterialPrefix())

    fun column(material: HTMaterialLike): Map<HTMaterialPrefix, V> = table.column(material.asMaterialKey())
}
