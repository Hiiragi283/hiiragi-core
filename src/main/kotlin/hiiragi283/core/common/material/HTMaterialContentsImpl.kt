package hiiragi283.core.common.material

import hiiragi283.core.api.collection.HTTable
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey

class HTMaterialContentsImpl<R : Any, V : Any>(
    table: HTTable<R, HTMaterialKey, V>,
    private val errorFactory: (R, HTMaterialKey) -> String,
) : HTMaterialContents<R, V>,
    HTTable<R, HTMaterialKey, V> by table {
    override fun getErrorMessage(row: R, material: HTMaterialKey): String = errorFactory(row, material.asMaterialKey())
}
