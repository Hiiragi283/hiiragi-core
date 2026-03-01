package hiiragi283.core.api.material

import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartLike
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

interface HTMaterialAccess {
    val blocks: HTMaterialContents<HTPart, Block>

    val items: HTMaterialContents<HTPart, Item>

    val tools: HTMaterialContents<HTToolType, Item>

    @Suppress("DEPRECATION")
    fun getBlockOrItem(part: HTPartLike, material: HTMaterialLike): HTMaterialContents.Entry<out ItemLike>? =
        blocks[part, material] ?: items[part, material]
}
