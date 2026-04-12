package hiiragi283.core.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.integration.jei.category.HTLookupRecipeCategory
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole

class HCExplodingRecipeCategory(guiHelper: IGuiHelper) :
    HTLookupRecipeCategory<HCExplodingRecipe>(guiHelper, HCRecipeViewerTypes.EXPLODING) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HCExplodingRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addItemIngredient(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addSlot(RecipeIngredientRole.RENDER_ONLY, getPosition(2), getPosition(0))
            .addItemStack(HCExplodingRecipe.createIcon(recipe.minPower.toFloat()))
            .setSlotBackground(HTBackgroundType.NONE)
        // output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .addItemResult(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtrasImpl(builder: IRecipeExtrasBuilder, recipe: HCExplodingRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(1))
    }
}
