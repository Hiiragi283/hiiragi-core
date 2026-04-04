package hiiragi283.core.client.integration.jei.extension

import hiiragi283.core.api.integration.jei.add
import hiiragi283.core.client.integration.jei.extension.base.HTItemToChancedRecipeCategoryExtension
import hiiragi283.core.impl.recipe.HTBasicItemToChancedRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor

class HTBasicItemToChancedRecipeCategoryExtension<RECIPE : HTBasicItemToChancedRecipe> :
    HTItemToChancedRecipeCategoryExtension<RECIPE> {
    override fun <T : IIngredientAcceptor<T>> setInput(recipe: RECIPE, accessor: T) {
        accessor.add(recipe.ingredient)
    }

    override fun <T : IIngredientAcceptor<T>> setOutput(recipe: RECIPE, accessor: T) {
        accessor.add(recipe.result)
    }

    override fun <T : IIngredientAcceptor<T>> setExtraOutput(recipe: RECIPE, accessor: T) {
        recipe.extraResult.ifPresent(accessor::add)
    }
}
