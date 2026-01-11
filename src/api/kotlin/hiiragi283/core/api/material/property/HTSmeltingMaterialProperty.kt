package hiiragi283.core.api.material.property

import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike

/**
 * 製錬レシピの情報を保持するクラスです。
 * @param prefix 製錬後のプレフィックス
 * @param key 製錬後の素材
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
class HTSmeltingMaterialProperty private constructor(
    val prefix: HTMaterialPrefix?,
    val key: HTMaterialKey?,
    val isBlasting: Boolean,
    val isSmoking: Boolean,
) {
    companion object {
        /**
         * 製錬レシピを無効にする[HTSmeltingMaterialProperty]のインスタンスを作成します。
         */
        @JvmStatic
        fun disable(): HTSmeltingMaterialProperty = HTSmeltingMaterialProperty(null, null, isBlasting = false, isSmoking = false)

        /**
         * かまどレシピのみを登録する[HTSmeltingMaterialProperty]のインスタンスを作成します。
         */
        @JvmStatic
        fun smeltingOnly(prefix: HTPrefixLike, material: HTMaterialLike): HTSmeltingMaterialProperty =
            HTSmeltingMaterialProperty(prefix.asMaterialPrefix(), material.asMaterialKey(), isBlasting = false, isSmoking = false)

        /**
         * かまどレシピと溶鉱炉レシピを登録する[HTSmeltingMaterialProperty]のインスタンスを作成します。
         */
        @JvmStatic
        fun withBlasting(prefix: HTPrefixLike, material: HTMaterialLike): HTSmeltingMaterialProperty =
            HTSmeltingMaterialProperty(prefix.asMaterialPrefix(), material.asMaterialKey(), isBlasting = true, isSmoking = false)

        /**
         * かまどレシピと燻製器レシピを登録する[HTSmeltingMaterialProperty]のインスタンスを作成します。
         */
        @JvmStatic
        fun withSmoking(prefix: HTPrefixLike, material: HTMaterialLike): HTSmeltingMaterialProperty =
            HTSmeltingMaterialProperty(prefix.asMaterialPrefix(), material.asMaterialKey(), isBlasting = false, isSmoking = true)
    }
}
