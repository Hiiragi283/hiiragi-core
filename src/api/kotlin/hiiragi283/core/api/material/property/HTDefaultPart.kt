package hiiragi283.core.api.material.property

import hiiragi283.core.api.material.HTMaterialContentsAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

sealed interface HTDefaultPart {
    fun getTag(key: HTMaterialKey): TagKey<Item>

    fun getItem(key: HTMaterialKey): HTItemHolderLike<*>?

    fun getSuffix(): String

    data class Tag(val tagKey: TagKey<Item>, val item: HTItemHolderLike<*>) : HTDefaultPart {
        override fun getTag(key: HTMaterialKey): TagKey<Item> = tagKey

        override fun getItem(key: HTMaterialKey): HTItemHolderLike<*> = item

        override fun getSuffix(): String = tagKey.location().path
    }

    enum class Prefixed : HTDefaultPart {
        CROP,
        DUST,
        FUEL,
        GEM,
        INGOT,
        PEARL,
        PLATE,
        ;

        val prefix: HTTagPrefix get() = when (this) {
            CROP -> CommonTagPrefixes.CROP
            DUST -> CommonTagPrefixes.DUST
            FUEL -> CommonTagPrefixes.FUEL
            GEM -> CommonTagPrefixes.GEM
            INGOT -> CommonTagPrefixes.INGOT
            PEARL -> CommonTagPrefixes.PEARL
            PLATE -> CommonTagPrefixes.PLATE
        }

        override fun getTag(key: HTMaterialKey): TagKey<Item> = prefix.itemTagKey(key)

        override fun getItem(key: HTMaterialKey): HTItemHolderLike<*>? = HTMaterialContentsAccess.INSTANCE.getBlock(prefix, key)
            ?: HTMaterialContentsAccess.INSTANCE.getItem(prefix, key)
            ?: HTMaterialContentsAccess.INSTANCE.getVanillaTable()[prefix, key]

        override fun getSuffix(): String = prefix.name
    }
}
