package hiiragi283.core.client.jei.extension

import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.impl.recipe.HTBasicDoubleMultiOutputRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import kotlin.jvm.optionals.getOrNull

class HTBasicDoubleMultiOutputRecipeCategoryExtension<RECIPE : HTBasicDoubleMultiOutputRecipe> :
    HTDoubleMultiOutputRecipeCategoryExtension<RECIPE> {
    override fun <T : IIngredientAcceptor<T>> setBase(recipe: RECIPE, accessor: T) {
        accessor.addItemIngredient(recipe.base)
    }

    override fun <T : IIngredientAcceptor<T>> setAddition(recipe: RECIPE, accessor: T) {
        accessor.addItemIngredient(recipe.addition.getOrNull())
    }

    override fun <T : IIngredientAcceptor<T>> setOutput(recipe: RECIPE, index: Int, accessor: T) {
        val result: HTItemResult = recipe.results.getOrNull(index) ?: return
        accessor.addItemResult(result)
    }
}
