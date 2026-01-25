package hiiragi283.core.api.material.property

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.item.tool.HTToolMaterial
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.resources.ResourceLocation

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[HTPropertyKey]の一覧をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
object HTMaterialPropertyKeys {
    /**
     * デフォルトの部品に対応する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val DEFAULT_PART: HTPropertyKey<HTDefaultPart?> = createNullable("default_part")

    /**
     * デフォルトの[部品][HTTagPrefix]あたりの液体量に対応する[プロパティキー][HTPropertyKey]
     * @since 0.7.0
     */
    @JvmField
    val DEFAULT_FLUID_AMOUNT: HTPropertyKey<Int> =
        HTPropertyKey.create(HiiragiCoreAPI.id("default_fluid_amount"), HTConst.INGOT_AMOUNT)

    /**
     * 粉砕後の[HTTagPrefix]に対応する[プロパティキー][HTPropertyKey]
     * @since 0.8.0
     */
    @JvmField
    val CRUSHED_PREFIX: HTPropertyKey<HTTagPrefix> =
        HTPropertyKey.create(HiiragiCoreAPI.id("crushed_prefix"), CommonTagPrefixes.DUST)

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

    //    Registration    //

    /**
     * 登録する素材ブロックに使われる[プロパティキー][HTPropertyKey]
     * @since 0.7.0
     */
    @JvmField
    val BLOCK_PREFIXES: HTPropertyKey<Set<HTTagPrefix>> = HTPropertyKey.createSet(HiiragiCoreAPI.id("block_prefixes"))

    /**
     * 登録する素材アイテムに使われる[プロパティキー][HTPropertyKey]
     * @since 0.7.0
     */
    @JvmField
    val ITEM_PREFIXES: HTPropertyKey<Set<HTTagPrefix>> = HTPropertyKey.createSet(HiiragiCoreAPI.id("item_prefixes"))

    /**
     * 登録する素材ツールに使われる[プロパティキー][HTPropertyKey]
     * @since 0.8.0
     */
    @JvmField
    val TOOL_PREFIXES: HTPropertyKey<Set<HTToolType>> = HTPropertyKey.createSet(HiiragiCoreAPI.id("tool_prefixes"))

    /**
     * ツールの素材に対応する[プロパティキー][HTPropertyKey]
     * @since 0.8.0
     */
    @JvmField
    val TOOL_MATERIAL: HTPropertyKey<HTToolMaterial?> = createNullable("tool_material")

    //    Data Gen    //

    /**
     * 素材の[翻訳名][HTLangName]を保持する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val LANG_NAME: HTPropertyKey<HTLangName?> = createNullable("lang_name")

    /**
     * [プレフィックス][HTTagPrefix]に依存する[翻訳名][HTLangName]を保持する[プロパティキー][HTPropertyKey]
     * @since 0.7.0
     */
    @JvmField
    val CUSTOM_LANG_NAME: HTPropertyKey<Map<HTTagPrefix, HTLangName>> = HTPropertyKey.createMap(HiiragiCoreAPI.id("custom_lang_name"))

    /**
     * かまど燃料としての時間に対応する[プロパティキー][HTPropertyKey]
     * @since 0.8.0
     */
    @JvmField
    val FUEL_TIME: HTPropertyKey<Int?> = createNullable("fuel_time")

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
