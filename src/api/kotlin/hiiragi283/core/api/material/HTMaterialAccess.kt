package hiiragi283.core.api.material

import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartLike

interface HTMaterialAccess {
    val blocks: HTMaterialContents<HTPart, HTMaterialContents.BlockEntry>

    val items: HTMaterialContents<HTPart, HTMaterialContents.ItemEntry>

    val tools: HTMaterialContents<HTToolType, HTMaterialContents.ItemEntry>

    fun getBlockOrItem(part: HTPartLike, material: HTMaterialLike): HTMaterialContents.ItemEntry? = blocks[part, material]?.let { HTMaterialContents.ItemEntry(it.getItemSupplier(), it.isBuiltIn) } ?: items[part, material]
}
