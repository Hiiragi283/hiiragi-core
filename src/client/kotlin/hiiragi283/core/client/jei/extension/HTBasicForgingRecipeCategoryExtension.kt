package hiiragi283.core.client.jei.extension

import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.impl.recipe.HTBasicForgingRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import kotlin.jvm.optionals.getOrNull

data object HTBasicForgingRecipeCategoryExtension : HTForgingRecipeCategoryExtension<HTBasicForgingRecipe> {
    override fun <T : IIngredientAcceptor<T>> setBase(recipe: HTBasicForgingRecipe, accessor: T) {
        accessor.addItemIngredient(recipe.base)
    }

    override fun <T : IIngredientAcceptor<T>> setAdditional(recipe: HTBasicForgingRecipe, accessor: T) {
        accessor.addItemIngredient(recipe.addition.getOrNull())
    }

    override fun <T : IIngredientAcceptor<T>> setOutput(recipe: HTBasicForgingRecipe, index: Int, accessor: T) {
        val result: HTItemResult = recipe.results.getOrNull(index) ?: return
        accessor.addItemResult(result)
    }
}
