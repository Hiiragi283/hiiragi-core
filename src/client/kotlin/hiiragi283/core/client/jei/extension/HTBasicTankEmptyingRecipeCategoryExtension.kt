package hiiragi283.core.client.jei.extension

import hiiragi283.core.api.integration.jei.addFluidResult
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.integration.jei.extension.HTTankEmptyingRecipeCategoryExtension
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import kotlin.jvm.optionals.getOrNull

data object HTBasicTankEmptyingRecipeCategoryExtension : HTTankEmptyingRecipeCategoryExtension<HCTankEmptyingRecipe> {
    override fun <T : IIngredientAcceptor<T>> setInput(recipe: HCTankEmptyingRecipe, accessor: T) {
        accessor.addItemIngredient(recipe.ingredient)
    }

    override fun <T : IIngredientAcceptor<T>> setItemOutput(recipe: HCTankEmptyingRecipe, accessor: T) {
        accessor.addItemResult(recipe.itemResult.getOrNull())
    }

    override fun <T : IIngredientAcceptor<T>> setFluidOutput(recipe: HCTankEmptyingRecipe, accessor: T) {
        accessor.addFluidResult(recipe.fluidResult, false)
    }
}
