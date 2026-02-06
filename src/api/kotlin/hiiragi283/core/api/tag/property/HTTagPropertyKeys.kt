package hiiragi283.core.api.tag.property

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.core.api.property.HTPropertyMap
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.state.BlockBehaviour
import org.apache.commons.lang3.math.Fraction

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[HTPropertyKey]の一覧をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
object HTTagPropertyKeys {
    /**
     * [ID][ResourceLocation]のパターンを管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val ID_PATTERN: HTPropertyKey<String?> = createNullable("id_pattern")

    /**
     * 基準値に対する数量を管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val ITEM_SCALE: HTPropertyKey<(Fraction, HTPropertyMap) -> Fraction> =
        HTPropertyKey.create(HiiragiCoreAPI.id("item_scale")) { base: Fraction, _ -> base }

    /**
     * @since 0.10.0
     */
    @JvmField
    val IS_RAW: HTPropertyKey<Unit?> = HTPropertyKey.createFlag(HiiragiCoreAPI.id("is_raw"))

    //    Block    //

    /**
     * ブロックの[プロパティ][BlockBehaviour.Properties]を管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val BLOCK_PROP: HTPropertyKey<BlockBehaviour.Properties?> = createNullable("block_properties")

    /**
     * 鉱石ブロックの母岩部分のテクスチャを管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val ORE_STONE_TEX: HTPropertyKey<ResourceLocation?> = createNullable("ore_stone_tex")

    //    Data Gen    //

    // Data Map
    @JvmField
    val FUEL_SCALE: HTPropertyKey<Fraction?> = createNullable("fuel_scale")

    // Lang
    /**
     * 翻訳のパターンを管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val LANG_PATTERN: HTPropertyKey<HTLangPatternProvider> =
        HTPropertyKey.create(HiiragiCoreAPI.id("lang_pattern"), HTLangPatternProvider.create("%s", "%s"))

    // Texture
    /**
     * テクスチャを生成する際のテクスチャの名前を管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val TEXTURE_ICON: HTPropertyKey<String?> = createNullable("texture_icon")

    /**
     * @since 0.10.0
     */
    @JvmField
    val DISABLE_TEXTURE_GEN: HTPropertyKey<Unit?> = HTPropertyKey.createFlag(HiiragiCoreAPI.id("disable_texture_gen"))

    @JvmStatic
    fun <T : Any> createNullable(path: String): HTPropertyKey<T?> = HTPropertyKey.createNullable(HiiragiCoreAPI.id(path))
}
