package hiiragi283.core.api.material

import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.core.Holder
import net.minecraft.world.item.Item

interface HTMaterialAccess {
    val blocks: HTMaterialContents<HTTagPrefix, HTBlockHolderLike<*>>

    val items: HTMaterialContents<HTTagPrefix, HTItemHolderLike<*>>

    val tools: HTMaterialContents<HTToolType, HTItemHolderLike<*>>

    @Suppress("DEPRECATION")
    fun getBlockOrItem(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*>? = blocks[prefix, material]?.let {
        object : HTItemHolderLike.Simple<Item> {
            override fun asItem(): Item = it.asBlock().asItem()

            override fun getItemHolder(): Holder<Item> = asItem().builtInRegistryHolder()
        }
    } ?: items[prefix, material]
}
