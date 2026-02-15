package hiiragi283.core.api.material

import hiiragi283.core.api.collection.HTTable

interface HTMaterialContents<R : Any, V : Any> : HTTable<R, HTMaterialKey, V> {
    operator fun get(row: R, material: HTMaterialLike): V? = this[row, material.asMaterialKey()]

    fun getOrThrow(row: R, material: HTMaterialLike): V = get(row, material) ?: error(getErrorMessage(row, material.asMaterialKey()))

    fun getErrorMessage(row: R, material: HTMaterialKey): String

    fun column(material: HTMaterialLike): Map<R, V> = this.column(material.asMaterialKey())
}
