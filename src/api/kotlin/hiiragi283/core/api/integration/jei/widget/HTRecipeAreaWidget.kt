package hiiragi283.core.api.integration.jei.widget

import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType

/**
 * @see mekanism.client.recipe_viewer.interfaces.IRecipeViewerRecipeArea<*>
 */
interface HTRecipeAreaWidget<WIDGET : HTWidget> {
    fun getSupportedRecipeTypes(): Iterable<HTRecipeViewerType<*>>

    fun setSupportedRecipeTypes(vararg recipeTypes: HTRecipeViewerType<*>): WIDGET = setSupportedRecipeTypes(recipeTypes.toList())

    fun setSupportedRecipeTypes(recipeTypes: Iterable<HTRecipeViewerType<*>>): WIDGET

    fun isRecipeAreaActive(): Boolean = true
}
