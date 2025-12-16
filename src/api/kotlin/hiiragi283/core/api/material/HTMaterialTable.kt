package hiiragi283.core.api.material

import hiiragi283.core.api.collection.HTTable

@JvmInline
value class HTMaterialTable<R : Any, V : Any>(private val table: HTTable<R, HTMaterialKey, V>) : HTTable<R, HTMaterialKey, V> by table {
    operator fun get(row: R, material: HTMaterialLike): V? = this[row, material.asMaterialKey()]

    fun column(material: HTMaterialLike): Map<R, V> = this.column(material.asMaterialKey())
}
