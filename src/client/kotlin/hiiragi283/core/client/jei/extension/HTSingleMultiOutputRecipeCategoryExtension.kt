package hiiragi283.core.client.jei.extension

import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.recipe.IFocusGroup

interface HTSingleMultiOutputRecipeCategoryExtension<RECIPE : HTSingleMultiOutputRecipe> {
    fun <T : IIngredientAcceptor<T>> setInput(recipe: RECIPE, accessor: T)

    fun <T : IIngredientAcceptor<T>> setOutput(recipe: RECIPE, index: Int, accessor: T) {}

    fun onDisplayedIngredientsUpdate(
        recipe: RECIPE,
        inputSlot: IRecipeSlotDrawable,
        outputSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {}
}
