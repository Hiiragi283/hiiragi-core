package hiiragi283.core.api.integration.jei.extension

import hiiragi283.core.api.recipe.base.HTDoubleMultiOutputRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.recipe.IFocusGroup

interface HTDoubleMultiOutputRecipeCategoryExtension<RECIPE : HTDoubleMultiOutputRecipe> {
    fun <T : IIngredientAcceptor<T>> setBase(recipe: RECIPE, accessor: T)

    fun <T : IIngredientAcceptor<T>> setAddition(recipe: RECIPE, accessor: T)

    fun <T : IIngredientAcceptor<T>> setOutput(recipe: RECIPE, index: Int, accessor: T) {}

    fun onDisplayedIngredientsUpdate(
        recipe: RECIPE,
        baseSlot: IRecipeSlotDrawable,
        additionSlot: IRecipeSlotDrawable,
        outputSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {}
}
