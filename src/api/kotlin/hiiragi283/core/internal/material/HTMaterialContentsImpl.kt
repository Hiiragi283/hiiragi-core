package hiiragi283.core.internal.material

import hiiragi283.core.api.collection.Table
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey

internal class HTMaterialContentsImpl<R : Any, out V>(
    table: Table<R, HTMaterialKey, V>,
    private val errorFactory: (R, HTMaterialKey) -> String,
) : HTMaterialContents<R, V>,
    Table<R, HTMaterialKey, V> by table {
    override fun getErrorMessage(row: R, key: HTMaterialKey): String = errorFactory(row, key)
}
