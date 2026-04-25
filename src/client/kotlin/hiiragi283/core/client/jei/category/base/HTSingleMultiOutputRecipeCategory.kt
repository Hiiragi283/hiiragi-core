package hiiragi283.core.client.jei.category.base

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.recipe.viewer.display.HTProcessingRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

abstract class HTSingleMultiOutputRecipeCategory(guiHelper: IGuiHelper, recipeType: HTRecipeViewerType<HTProcessingRecipeDisplay>) :
    HTMultiOutputRecipeCategory(guiHelper, recipeType) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0.5))
            .addItemStacks(contents.inputItem(0))
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        setupOutputs(builder, contents, focuses)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTProcessingRecipeDisplay, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(1.25), getPosition(0.5))
    }
}
