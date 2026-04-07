package hiiragi283.core.client.jei.extension

import hiiragi283.core.api.recipe.HTSingleItemRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.recipe.IFocusGroup

interface HTSingleItemRecipeCategoryExtension<RECIPE : HTSingleItemRecipe> {
    fun <T : IIngredientAcceptor<T>> setInput(recipe: RECIPE, accessor: T)

    fun <T : IIngredientAcceptor<T>> setOutput(recipe: RECIPE, accessor: T) {}

    fun onDisplayedIngredientsUpdate(
        recipe: RECIPE,
        inputSlot: IRecipeSlotDrawable,
        outputSlot: IRecipeSlotDrawable,
        focuses: IFocusGroup,
    ) {}
}
