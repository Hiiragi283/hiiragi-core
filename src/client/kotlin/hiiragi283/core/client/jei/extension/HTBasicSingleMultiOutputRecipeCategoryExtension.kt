package hiiragi283.core.client.jei.extension

import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.integration.jei.extension.HTSingleMultiOutputRecipeCategoryExtension
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.impl.recipe.HTBasicSingleMultiOutputRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor

class HTBasicSingleMultiOutputRecipeCategoryExtension<RECIPE : HTBasicSingleMultiOutputRecipe> :
    HTSingleMultiOutputRecipeCategoryExtension<RECIPE> {
    override fun <T : IIngredientAcceptor<T>> setInput(recipe: RECIPE, accessor: T) {
        accessor.addItemIngredient(recipe.ingredient)
    }

    override fun <T : IIngredientAcceptor<T>> setOutput(recipe: RECIPE, index: Int, accessor: T) {
        val result: HTItemResult = recipe.results.getOrNull(index) ?: return
        accessor.addItemResult(result)
    }
}
