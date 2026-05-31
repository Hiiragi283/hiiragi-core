package hiiragi283.core.client.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.integration.jei.category.HTDisplayRecipeCategory
import hiiragi283.lib.recipe.viewer.HTRecipeViewerType
import hiiragi283.lib.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.lib.recipe.viewer.display.HTRecipeContents
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTItemToChancedItemsRecipeCategory(guiHelper: IGuiHelper, recipeType: HTRecipeViewerType<HTProgressRecipeDisplay>) : HTDisplayRecipeCategory.Progress(guiHelper, recipeType) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0.5))
            .addItemStacks(contents.inputItem(0))
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        repeat(4) { index: Int ->
            builder
                .addOutputSlot(getPosition(3 + index % 2), getPosition(0 + index / 2))
                .add(contents.outputItem(index))
                .setSlotBackground(HTBackgroundType.OUTPUT)
        }
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTProgressRecipeDisplay, focuses: IFocusGroup) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0.5))
    }
}
