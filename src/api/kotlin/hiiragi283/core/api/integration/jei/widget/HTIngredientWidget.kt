package hiiragi283.core.api.integration.jei.widget

/**
 * レシピビューワーに保持しているオブジェクトを提供可能ウィジェットを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 * @see mekanism.client.recipe_viewer.interfaces.IRecipeViewerIngredientHelper
 */
fun interface HTIngredientWidget {
    /**
     * 保持しているオブジェクトを取得します。
     * @return 空の場合は`null`
     */
    fun getIngredient(): Any?
}
