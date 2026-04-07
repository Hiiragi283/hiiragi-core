package hiiragi283.core.client.jei.extension

import hiiragi283.core.api.recipe.HTDoubleItemToMultiOutputRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.recipe.IFocusGroup
import java.util.function.IntFunction

interface HTDoubleItemToMultiOutputRecipeCategoryExtension<RECIPE : HTDoubleItemToMultiOutputRecipe> {
    fun <T : IIngredientAcceptor<T>> setBase(recipe: RECIPE, accessor: T)

    fun <T : IIngredientAcceptor<T>> setAdditional(recipe: RECIPE, accessor: T)

    fun <T : IIngredientAcceptor<T>> setOutput(recipe: RECIPE, index: Int, accessor: T) {}

    fun onDisplayedIngredientsUpdate(
        recipe: RECIPE,
        baseSlot: IRecipeSlotDrawable,
        additionSlot: IRecipeSlotDrawable,
        outputSlots: IntFunction<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {}
}
