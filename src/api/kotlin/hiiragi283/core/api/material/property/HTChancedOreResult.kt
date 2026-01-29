package hiiragi283.core.api.material.property

import hiiragi283.core.api.data.recipe.HTResultCreator
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.tag.HTTagPrefix
import org.apache.commons.lang3.math.Fraction

/**
 * 鉱石の粉砕レシピにおける副産物を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
@JvmRecord
data class HTChancedOreResult(val prefix: HTTagPrefix, val key: HTMaterialKey, val chance: Fraction) {
    /**
     * [HTChancedItemResult]に変換します。
     */
    fun toResult(creator: HTResultCreator): HTChancedItemResult = HTChancedItemResult(creator.material(prefix, key), chance)
}
