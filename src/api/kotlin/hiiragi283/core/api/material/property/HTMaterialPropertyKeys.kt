package hiiragi283.core.api.material.property

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.property.HTPropertyKey
import net.minecraft.resources.ResourceLocation

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[HTPropertyKey]の一覧をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
object HTMaterialPropertyKeys {
    /**
     * デフォルトの[部品][HTMaterialPrefix]に対応する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val DEFAULT_PART: HTPropertyKey<HTMaterialPrefix?> = createNullable("default_part")

    /**
     * 溶融状態の[液体][HTFluidMaterialProperty]に対応する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val MOLTEN_FLUID: HTPropertyKey<HTFluidMaterialProperty?> = createNullable("molten_fluid")

    /**
     * ブロックの管理に使用される[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val STORAGE_BLOCK: HTPropertyKey<HTStorageBlockProperty> =
        HTPropertyKey.create(HiiragiCoreAPI.id("storage_block"), HTStorageBlockProperty.THREE_BY_THREE)

    //    Data Gen    //

    /**
     * 素材の[翻訳名][HTLangName]を保持する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val LANG_NAME: HTPropertyKey<HTLangName?> = createNullable("lang_name")

    /**
     * 製錬レシピの管理に使用される[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val SMELTING: HTPropertyKey<HTSmeltingMaterialProperty?> = createNullable("smelting")

    /**
     * テクスチャを生成する際のカラーパレットの[ID][ResourceLocation]に対応する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val TEXTURE_COLOR: HTPropertyKey<ResourceLocation?> = createNullable("texture_color")

    /**
     * テクスチャを生成する際のテンプレートに対応する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val TEXTURE_SET: HTPropertyKey<HTMaterialTextureSet> =
        HTPropertyKey.create(HiiragiCoreAPI.id("texture_set"), HTMaterialTextureSet.DEFAULT)

    @JvmStatic
    fun <T : Any> createNullable(path: String): HTPropertyKey<T?> = HTPropertyKey.createNullable(HiiragiCoreAPI.id(path))
}
