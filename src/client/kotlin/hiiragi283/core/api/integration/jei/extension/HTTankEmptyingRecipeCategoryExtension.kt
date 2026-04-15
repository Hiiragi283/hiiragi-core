package hiiragi283.core.api.integration.jei.extension

import hiiragi283.core.api.recipe.base.HTTankEmptyingRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.recipe.IFocusGroup

interface HTTankEmptyingRecipeCategoryExtension<RECIPE : HTTankEmptyingRecipe> {
    fun <T : IIngredientAcceptor<T>> setInput(recipe: RECIPE, accessor: T)

    fun <T : IIngredientAcceptor<T>> setItemOutput(recipe: RECIPE, accessor: T) {}

    fun <T : IIngredientAcceptor<T>> setFluidOutput(recipe: RECIPE, accessor: T) {}

    fun onDisplayedIngredientsUpdate(
        recipe: RECIPE,
        inputSlot: IRecipeSlotDrawable,
        itemOutputSlot: IRecipeSlotDrawable,
        fluidOutputSlot: IRecipeSlotDrawable,
        focuses: IFocusGroup,
    ) {}
}
