package hiiragi283.core.api.integration.jei.extension

import hiiragi283.core.api.recipe.base.HTTankFillingRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.recipe.IFocusGroup

interface HTTankFillingRecipeCategoryExtension<RECIPE : HTTankFillingRecipe> {
    fun <T : IIngredientAcceptor<T>> setItemInput(recipe: RECIPE, accessor: T)

    fun <T : IIngredientAcceptor<T>> setFluidInput(recipe: RECIPE, accessor: T)

    fun <T : IIngredientAcceptor<T>> setOutput(recipe: RECIPE, accessor: T) {}

    fun onDisplayedIngredientsUpdate(
        recipe: RECIPE,
        itemInputSlot: IRecipeSlotDrawable,
        fluidInputSlot: IRecipeSlotDrawable,
        outputSlot: IRecipeSlotDrawable,
        focuses: IFocusGroup,
    ) {}
}
