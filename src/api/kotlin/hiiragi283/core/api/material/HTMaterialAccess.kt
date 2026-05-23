package hiiragi283.core.api.material

import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.resource.SupplierWithId
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

interface HTMaterialAccess {
    val blocks: HTSimpleMaterialContents<HTPart, Block>

    val items: HTMaterialContents<HTPart, HTMaterialContents.ItemEntry>

    val tools: HTMaterialContents<HTToolType, HTMaterialContents.ItemEntry>

    fun getBlockOrItem(part: HTPartLike, material: HTMaterialLike): HTMaterialContents.ItemEntry? = blocks[part, material]
        ?.let {
            val item: SupplierWithId<Item> = object : SupplierWithId<Item> {
                override fun get(): Item = it.get().asItem()

                override fun getId(): ResourceLocation = it.getId()
            }
            HTMaterialContents.ItemEntry(item, it.isBuiltIn)
        }
        ?: items[part, material]
}
