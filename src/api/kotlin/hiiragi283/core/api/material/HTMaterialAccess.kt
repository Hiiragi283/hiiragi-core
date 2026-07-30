package hiiragi283.core.api.material

import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartLike

@JvmRecord
data class HTMaterialAccess(
    val blocks: HTMaterialContents<HTPart, HTMaterialContents.BlockEntry>,
    val items: HTMaterialContents<HTPart, HTMaterialContents.ItemEntry>,
    val tools: HTMaterialContents<HTToolType, HTMaterialContents.ItemEntry>,
) {
    fun getBlockOrItem(part: HTPartLike, key: HTMaterialKey): HTMaterialContents.ItemEntry? = blocks[part, key]?.let { HTMaterialContents.ItemEntry(it.getItemSupplier(), it.isBuiltIn) } ?: items[part, key]
}
