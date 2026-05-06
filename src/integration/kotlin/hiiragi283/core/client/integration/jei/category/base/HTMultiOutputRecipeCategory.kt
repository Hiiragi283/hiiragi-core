package hiiragi283.core.client.integration.jei.category.base

import hiiragi283.core.api.integration.jei.category.HTDisplayRecipeCategory
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

abstract class HTMultiOutputRecipeCategory(guiHelper: IGuiHelper, recipeType: HTRecipeViewerType<HTProgressRecipeDisplay>) :
    HTDisplayRecipeCategory.Progress(guiHelper, recipeType) {
    protected abstract fun setupOutputs(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup)
}
