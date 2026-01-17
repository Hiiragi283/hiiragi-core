package hiiragi283.core.api.material.property

import hiiragi283.core.api.registry.HTItemHolderLike

/**
 * 製錬レシピの情報を保持するクラスです。
 * @param result 製錬後の完成品
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
class HTSmeltingMaterialProperty private constructor(val result: HTItemHolderLike<*>?, val isBlasting: Boolean, val isSmoking: Boolean) {
    companion object {
        /**
         * 製錬レシピを無効にする[HTSmeltingMaterialProperty]のインスタンスを作成します。
         */
        @JvmStatic
        fun disable(): HTSmeltingMaterialProperty = HTSmeltingMaterialProperty(null, isBlasting = false, isSmoking = false)

        /**
         * かまどレシピのみを登録する[HTSmeltingMaterialProperty]のインスタンスを作成します。
         */
        @JvmStatic
        fun smeltingOnly(result: HTItemHolderLike<*>): HTSmeltingMaterialProperty =
            HTSmeltingMaterialProperty(result, isBlasting = false, isSmoking = false)

        /**
         * かまどレシピと溶鉱炉レシピを登録する[HTSmeltingMaterialProperty]のインスタンスを作成します。
         */
        @JvmStatic
        fun withBlasting(result: HTItemHolderLike<*>): HTSmeltingMaterialProperty =
            HTSmeltingMaterialProperty(result, isBlasting = true, isSmoking = false)

        /**
         * かまどレシピと燻製器レシピを登録する[HTSmeltingMaterialProperty]のインスタンスを作成します。
         */
        @JvmStatic
        fun withSmoking(result: HTItemHolderLike<*>): HTSmeltingMaterialProperty =
            HTSmeltingMaterialProperty(result, isBlasting = false, isSmoking = true)
    }
}
