package hiiragi283.core.api.tag.property

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.core.api.property.HTPropertyMap
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.state.BlockBehaviour

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[HTPropertyKey]の一覧をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
object HTTagPropertyKeys {
    /**
     * [ID][ResourceLocation]のパターンに対応する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val ID_PATTERN: HTPropertyKey<String?> = createNullable("id_pattern")

    /**
     * 共通タグの[ID][ResourceLocation]のパターンに対応する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val COMMON_TAG_PATTERN: HTPropertyKey<String?> = createNullable("common_tag_pattern")

    /**
     * 素材付き共通タグの[ID][ResourceLocation]のパターンに対応する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val TAG_PATTERN: HTPropertyKey<String?> = createNullable("tag_pattern")

    /**
     * 基準値に対する数量を取得する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val ITEM_SCALE: HTPropertyKey<(Int, HTPropertyMap) -> Int> =
        HTPropertyKey.create(HiiragiCoreAPI.id("item_scale")) { base: Int, _ -> base }

    /**
     * ブロックの[プロパティ][BlockBehaviour.Properties]に対応する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val BLOCK_PROP: HTPropertyKey<BlockBehaviour.Properties?> = createNullable("block_properties")

    /**
     * 鉱石ブロックの母岩部分のテクスチャに対応する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val ORE_STONE_TEX: HTPropertyKey<ResourceLocation?> = createNullable("ore_stone_tex")

    //    Data Gen    //

    /**
     * 翻訳のパターンに対応する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val LANG_PATTERN: HTPropertyKey<HTLangPatternProvider> =
        HTPropertyKey.create(HiiragiCoreAPI.id("lang_pattern"), HTLangPatternProvider.create("%s", "%s"))

    /**
     * テクスチャを生成する際のテクスチャの名前に対応する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val TEXTURE_ICON: HTPropertyKey<String?> = createNullable("texture_icon")

    @JvmStatic
    fun <T : Any> createNullable(path: String): HTPropertyKey<T?> = HTPropertyKey.createNullable(HiiragiCoreAPI.id(path))
}
