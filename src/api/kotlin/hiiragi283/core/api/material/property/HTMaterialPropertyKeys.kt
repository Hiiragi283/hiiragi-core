package hiiragi283.core.api.material.property

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.texture.HTColorPalette
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.property.HTPropertyKey

object HTMaterialPropertyKeys {
    @JvmField
    val DEFAULT_PART: HTPropertyKey<HTMaterialPrefix?> = createNullable("default_part")

    @JvmField
    val MOLTEN_FLUID: HTPropertyKey<HTFluidMaterialProperty?> = createNullable("molten_fluid")

    @JvmField
    val SMELTING: HTPropertyKey<HTSmeltingMaterialProperty?> = createNullable("smelting")

    @JvmField
    val STORAGE_BLOCK: HTPropertyKey<HTStorageBlockProperty> =
        HTPropertyKey.create(HiiragiCoreAPI.id("storage_block"), HTStorageBlockProperty.THREE_BY_THREE)

    //    Data Gen    //

    @JvmField
    val LANG_NAME: HTPropertyKey<HTLangName?> = createNullable("lang_name")

    @JvmField
    val TEXTURE_COLOR: HTPropertyKey<HTColorPalette?> = createNullable("texture_color")

    @JvmField
    val TEXTURE_TEMPLATE: HTPropertyKey<HTTextureTemplate?> = createNullable("texture_template")

    @JvmStatic
    fun <T : Any> createNullable(path: String): HTPropertyKey<T?> = HTPropertyKey.createNullable(HiiragiCoreAPI.id(path))
}
