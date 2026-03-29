package hiiragi283.core.api.integration.jei.widget

import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType

/**
 * クリックするとレシピビューワー上でレシピを表示可能なウィジェットを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 * @see mekanism.client.recipe_viewer.interfaces.IRecipeViewerRecipeArea<*>
 */
interface HTRecipeAreaWidget<WIDGET : HTWidget> {
    /**
     * サポートしているレシピの種類を取得します。
     * @return [HTRecipeViewerType]の一覧
     */
    fun getSupportedRecipeTypes(): Iterable<HTRecipeViewerType<*>>

    /**
     * サポートするレシピの種類を追加します。
     * @return この[ウィジェット][WIDGET]のインスタンス
     */
    fun setSupportedRecipeTypes(vararg recipeTypes: HTRecipeViewerType<*>): WIDGET = setSupportedRecipeTypes(recipeTypes.toList())

    /**
     * サポートするレシピの種類を追加します。
     * @return この[ウィジェット][WIDGET]のインスタンス
     */
    fun setSupportedRecipeTypes(recipeTypes: Iterable<HTRecipeViewerType<*>>): WIDGET

    fun isRecipeAreaActive(): Boolean = true
}
