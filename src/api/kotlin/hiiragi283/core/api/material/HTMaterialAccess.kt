package hiiragi283.core.api.material

import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

interface HTMaterialAccess {
    val blocks: HTMaterialContents<HTTagPrefix, Block>

    val items: HTMaterialContents<HTTagPrefix, Item>

    val tools: HTMaterialContents<HTToolType, Item>

    @Suppress("DEPRECATION")
    fun getBlockOrItem(prefix: HTTagPrefix, material: HTMaterialLike): HTMaterialContents.Entry<out ItemLike>? =
        blocks[prefix, material] ?: items[prefix, material]
}
