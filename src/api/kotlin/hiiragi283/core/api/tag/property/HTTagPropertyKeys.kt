package hiiragi283.core.api.tag.property

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.core.api.property.HTPropertyMap

object HTTagPropertyKeys {
    @JvmField
    val ID_PATTERN: HTPropertyKey<String?> = createNullable("id_pattern")

    @JvmField
    val COMMON_TAG_PATTERN: HTPropertyKey<String?> = createNullable("common_tag_pattern")

    @JvmField
    val TAG_PATTERN: HTPropertyKey<String?> = createNullable("tag_pattern")

    @JvmField
    val ITEM_SCALE: HTPropertyKey<(Int, HTPropertyMap) -> Int> =
        HTPropertyKey.create(HiiragiCoreAPI.id("item_scale")) { base: Int, _ -> base }

    //    Data Gen    //

    @JvmField
    val LANG_PATTERN: HTPropertyKey<HTLangPatternProvider> =
        HTPropertyKey.create(HiiragiCoreAPI.id("lang_pattern"), HTLangPatternProvider.create("%s", "%s"))

    @JvmField
    val TEXTURE_ICON: HTPropertyKey<String?> = createNullable("texture_icon")

    @JvmStatic
    fun <T : Any> createNullable(path: String): HTPropertyKey<T?> = HTPropertyKey.createNullable(HiiragiCoreAPI.id(path))
}
