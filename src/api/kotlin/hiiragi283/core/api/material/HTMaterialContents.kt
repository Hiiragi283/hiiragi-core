package hiiragi283.core.api.material

import hiiragi283.core.api.collection.HTMapLike
import hiiragi283.core.api.collection.HTTableLike

interface HTMaterialContents<R : Any, V : Any> : HTTableLike<R, HTMaterialKey, V> {
    operator fun get(row: R, material: HTMaterialLike): V? = this[row, material.asMaterialKey()]

    fun getOrThrow(row: R, material: HTMaterialLike): V = get(row, material) ?: error(getErrorMessage(row, material.asMaterialKey()))

    fun getErrorMessage(row: R, material: HTMaterialKey): String

    fun column(material: HTMaterialLike): HTMapLike<R, V> = this.column(material.asMaterialKey())
}
