package hiiragi283.core.client.jei.extension

import hiiragi283.core.api.data.tank.HTTankInteraction
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.recipe.IFocusGroup

interface HTTankInteractionCategoryExtension<RECIPE : HTTankInteraction> {
    val canFill: Boolean
    val canEmpty: Boolean

    fun <T : IIngredientAcceptor<T>> setEmptyContainer(recipe: RECIPE, accessor: T) {}

    fun <T : IIngredientAcceptor<T>> setFilledContainer(recipe: RECIPE, accessor: T) {}

    fun <T : IIngredientAcceptor<T>> setFluid(recipe: RECIPE, accessor: T) {}

    fun onDisplayedIngredientsUpdate(
        recipe: RECIPE,
        emptySlot: IRecipeSlotDrawable,
        filledSlot: IRecipeSlotDrawable,
        fluidSlot: IRecipeSlotDrawable,
        focuses: IFocusGroup,
    ) {}
}
