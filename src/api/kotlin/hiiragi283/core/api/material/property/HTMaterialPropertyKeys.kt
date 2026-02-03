package hiiragi283.core.api.material.property

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.item.tool.HTToolMaterial
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[HTPropertyKey]の一覧をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
object HTMaterialPropertyKeys {
    /**
     * デフォルトの部品を管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val DEFAULT_PART: HTPropertyKey<HTDefaultPart?> = createNullable("default_part")

    /**
     * デフォルトの[部品][HTTagPrefix]あたりの液体量を管理する[プロパティキー][HTPropertyKey]
     * @since 0.7.0
     */
    @JvmField
    val DEFAULT_FLUID_AMOUNT: HTPropertyKey<Int> =
        HTPropertyKey.create(HiiragiCoreAPI.id("default_fluid_amount"), HTConst.INGOT_AMOUNT)

    //    Registration    //

    /**
     * 登録する素材ブロックを管理する[プロパティキー][HTPropertyKey]
     * @since 0.7.0
     */
    @JvmField
    val BLOCK_PREFIXES: HTPropertyKey<Set<HTTagPrefix>> = HTPropertyKey.createSet(HiiragiCoreAPI.id("block_prefixes"))

    /**
     * 登録する素材アイテムを管理する[プロパティキー][HTPropertyKey]
     * @since 0.7.0
     */
    @JvmField
    val ITEM_PREFIXES: HTPropertyKey<Set<HTTagPrefix>> = HTPropertyKey.createSet(HiiragiCoreAPI.id("item_prefixes"))

    /**
     * 登録する素材ツールを管理する[プロパティキー][HTPropertyKey]
     * @since 0.8.0
     */
    @JvmField
    val TOOL_PREFIXES: HTPropertyKey<Set<HTToolType>> = HTPropertyKey.createSet(HiiragiCoreAPI.id("tool_prefixes"))

    /**
     * ツールの素材を管理する[プロパティキー][HTPropertyKey]
     * @since 0.8.0
     */
    @JvmField
    val TOOL_MATERIAL: HTPropertyKey<HTToolMaterial?> = createNullable("tool_material")

    //    Recipe    //

    /**
     * ブロックの必要素材数を管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val STORAGE_BLOCK: HTPropertyKey<HTStorageBlockProperty> =
        HTPropertyKey.create(HiiragiCoreAPI.id("storage_block"), HTStorageBlockProperty.THREE_BY_THREE)

    // Smelting
    /**
     * 精錬レシピを無効化する[プロパティキー][HTPropertyKey]
     * @since 0.9.0
     */
    @JvmField
    val DISABLE_SMELTING: HTPropertyKey<Unit?> = HTPropertyKey.createFlag(HiiragiCoreAPI.id("disable_smelting"))

    /**
     * 精錬後の素材を管理する[プロパティキー][HTPropertyKey]
     * @since 0.9.0
     */
    @JvmField
    val SMELTED_TO: HTPropertyKey<HTMaterialKey?> = createNullable("smelted_to")

    // Smithing
    /**
     * 鍛冶台レシピを管理する[プロパティキー][HTPropertyKey]
     * @since 0.9.0
     */
    @JvmField
    val SMITHING_RECIPE: HTPropertyKey<HTSmithingRecipeProperty?> = createNullable("smithing_recipe")

    // Processing
    /**
     * 粉砕後の[HTTagPrefix]を管理する[プロパティキー][HTPropertyKey]
     * @since 0.8.0
     */
    @JvmField
    val CRUSHED_PREFIX: HTPropertyKey<HTTagPrefix> =
        HTPropertyKey.create(HiiragiCoreAPI.id("crushed_prefix"), CommonTagPrefixes.DUST)

    /**
     * プレス加工などのレシピを無効化する[プロパティキー][HTPropertyKey]
     * @since 0.9.0
     */
    @JvmField
    val DISABLE_MECHANICAL: HTPropertyKey<Unit?> = HTPropertyKey.createFlag(HiiragiCoreAPI.id("disable_mechanical"))

    /**
     * 溶融などのレシピを無効化する[プロパティキー][HTPropertyKey]
     * @since 0.9.0
     */
    @JvmField
    val DISABLE_MELTING: HTPropertyKey<Unit?> = HTPropertyKey.createFlag(HiiragiCoreAPI.id("disable_melting"))

    /**
     * 溶融状態の[液体][HTFluidMaterialProperty]を管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val MOLTEN_FLUID: HTPropertyKey<HTFluidMaterialProperty?> = createNullable("molten_fluid")

    /**
     * 鉱石粉砕の副産物を管理する[プロパティキー][HTPropertyKey]
     * @since 0.8.0
     */
    @JvmField
    val ORE_EXTRA_RESULTS: HTPropertyKey<Set<HTChancedOreResult>> =
        HTPropertyKey.createSet(HiiragiCoreAPI.id("ore_extra_results"))

    /**
     * 鉱石粉砕の主産物の個数の倍率を管理する[プロパティキー][HTPropertyKey]
     * @since 0.8.0
     */
    @JvmField
    val ORE_RESULT_MULTIPLIER: HTPropertyKey<Fraction> =
        HTPropertyKey.create(HiiragiCoreAPI.id("ore_result_multiplier"), Fraction.ONE)

    //    Data Gen    //

    // Data Map
    /**
     * かまど燃料としての時間を管理する[プロパティキー][HTPropertyKey]
     * @since 0.8.0
     */
    @JvmField
    val FUEL_TIME: HTPropertyKey<Int?> = createNullable("fuel_time")

    // Lang
    /**
     * 素材の[翻訳名][HTLangName]を管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val LANG_NAME: HTPropertyKey<HTLangName?> = createNullable("lang_name")

    /**
     * [プレフィックス][HTTagPrefix]に依存する[翻訳名][HTLangName]を管理する[プロパティキー][HTPropertyKey]
     * @since 0.7.0
     */
    @JvmField
    val CUSTOM_LANG_NAME: HTPropertyKey<Map<HTTagPrefix, HTLangName>> = HTPropertyKey.createMap(HiiragiCoreAPI.id("custom_lang_name"))

    // Loot Table
    /**
     * ブロックのルートテーブルを管理する[プロパティキー][HTPropertyKey]
     * @since 0.9.0
     */
    @JvmField
    val BLOCK_LOOT: HTPropertyKey<Map<HTTagPrefix, HTBlockLootFactory>> = HTPropertyKey.createMap(HiiragiCoreAPI.id("block_loot"))

    // Tag

    // Texture
    /**
     * テクスチャを生成する際のカラーパレットの[ID][ResourceLocation]を管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val TEXTURE_COLOR: HTPropertyKey<ResourceLocation?> = createNullable("texture_color")

    /**
     * テクスチャを生成する際のテンプレートを管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val TEXTURE_SET: HTPropertyKey<HTMaterialTextureSet> =
        HTPropertyKey.create(HiiragiCoreAPI.id("texture_set"), HTMaterialTextureSet.DEFAULT)

    @JvmStatic
    fun <T : Any> createNullable(path: String): HTPropertyKey<T?> = HTPropertyKey.createNullable(HiiragiCoreAPI.id(path))
}
