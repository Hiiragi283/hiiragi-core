package hiiragi283.lib.recipe.viewer.widget

/**
 * レシピビューワーに保持しているオブジェクトを提供可能なウィジェットを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
fun interface HTIngredientWidget {
    /**
     * 保持しているオブジェクトを取得します。
     * @return 空の場合は`null`
     */
    fun getIngredient(): Any?
}
