package hiiragi283.core.api.material

import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.HTTagPrefix

interface HTMaterialAccess {
    val blocks: HTMaterialContents<HTTagPrefix, HTBlockHolderLike<*>>

    val items: HTMaterialContents<HTTagPrefix, HTItemHolderLike<*>>

    val tools: HTMaterialContents<HTToolType, HTItemHolderLike<*>>

    @Suppress("DEPRECATION")
    fun getBlockOrItem(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*>? =
        blocks[prefix, material]?.let(HTBlockHolderLike.Companion::wrap) ?: items[prefix, material]
}
