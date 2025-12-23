package hiiragi283.core.api.material

import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.texture.HTColorPalette
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixTemplateMap
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[HTMaterialLike]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.3.0
 */
interface HTAbstractMaterial :
    HTMaterialLike,
    HTLangName {
    /**
     * 基準となる[プレフィックス][HTMaterialPrefix]
     */
    val basePrefix: HTMaterialPrefix

    /**
     * テクスチャ生成時に使用される[カラーパレット][HTColorPalette]
     */
    val colorPalette: HTColorPalette

    /**
     * アイテムを生成できる[プレフィックス][HTMaterialPrefix]の一覧を取得します。
     */
    fun getItemPrefixesToGenerate(): Set<HTMaterialPrefix>

    /**
     * テクスチャの生成に使用するテンプレートを取得します。
     */
    fun getItemPrefixMap(): HTPrefixTemplateMap

    /**
     * 指定した[プレフィックス][HTMaterialPrefix]からブロックやアイテムのIDのパスを取得します。
     */
    fun createPath(prefix: HTMaterialPrefix): String = prefix.createPath(this)

    /**
     * レシピで使用される基本のタグを取得します。
     */
    fun getBaseIngredient(): TagKey<Item> = basePrefix.itemTagKey(this)
}
