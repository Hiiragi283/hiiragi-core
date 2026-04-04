package hiiragi283.core.client.jei.extension

import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.impl.recipe.HTBasicItemToItemRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor

class HTBasicItemToItemRecipeCategoryExtension<RECIPE : HTBasicItemToItemRecipe> : HTItemToItemRecipeCategoryExtension<RECIPE> {
    override fun <T : IIngredientAcceptor<T>> setInput(recipe: RECIPE, accessor: T) {
        accessor.addItemIngredient(recipe.ingredient)
    }

    override fun <T : IIngredientAcceptor<T>> setOutput(recipe: RECIPE, accessor: T) {
        accessor.addItemResult(recipe.result)
    }
}
