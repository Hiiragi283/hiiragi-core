package hiiragi283.core.api.material

import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.registry.toItemLike
import net.minecraft.world.level.block.Block

interface HTMaterialAccess {
    val blocks: HTSimpleMaterialContents<HTPart, Block>

    val items: HTMaterialContents<HTPart, HTMaterialContents.ItemEntry>

    val tools: HTMaterialContents<HTToolType, HTMaterialContents.ItemEntry>

    fun getBlockOrItem(part: HTPartLike, material: HTMaterialLike): HTMaterialContents.ItemEntry? = blocks[part, material]
        ?.let { HTMaterialContents.ItemEntry(it.get().toItemLike(), it.isBuiltIn) }
        ?: items[part, material]
}
