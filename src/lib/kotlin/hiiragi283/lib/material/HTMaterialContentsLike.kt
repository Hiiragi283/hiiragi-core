package hiiragi283.lib.material

import hiiragi283.lib.tag.createTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

interface HTMaterialContentsLike : HTMaterialLike {
    fun getRawEntry(key: HTMaterialPartKey): HTMaterialRawEntry?

    fun getEntry(key: HTMaterialPartKey): HTMaterialItemEntry? = getRawEntry(key)?.getLeft()

    fun getTagKey(key: HTMaterialPartKey): TagKey<Item>? = getRawEntry(key)?.getRight()

    fun getBlockTagKey(key: HTMaterialPartKey): TagKey<Block>? = getTagKey(key)?.let { Registries.BLOCK.createTagKey(it.location()) }
}
