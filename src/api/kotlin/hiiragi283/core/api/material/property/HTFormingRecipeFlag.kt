package hiiragi283.core.api.material.property

/**
 * 素材の加工レシピで使用されるフラグを表すクラスです。
 * @param mechanical プレスなどの機械加工を許可するかどうか
 * @param melting 溶融や成形などの溶融加工を許可するかどうか
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
class HTFormingRecipeFlag(val mechanical: Boolean, val melting: Boolean) {
    companion object {
        /**
         * すべての加工を拒否します。
         */
        @JvmStatic
        fun disableAll(): HTFormingRecipeFlag = HTFormingRecipeFlag(mechanical = false, melting = false)

        /**
         * 機械加工のみを許可します。
         */
        @JvmStatic
        fun pressOnly(): HTFormingRecipeFlag = HTFormingRecipeFlag(mechanical = true, melting = false)

        /**
         * 溶融加工のみを許可します。
         */
        @JvmStatic
        fun solidifyOnly(): HTFormingRecipeFlag = HTFormingRecipeFlag(mechanical = false, melting = true)

        /**
         * すべての加工を許可します。
         */
        @JvmStatic
        fun enableAll(): HTFormingRecipeFlag = HTFormingRecipeFlag(mechanical = true, melting = true)
    }
}
