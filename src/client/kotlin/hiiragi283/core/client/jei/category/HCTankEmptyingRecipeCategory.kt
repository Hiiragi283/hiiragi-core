package hiiragi283.core.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addChancedItem
import hiiragi283.core.api.integration.jei.addFluidStack
import hiiragi283.core.api.integration.jei.category.HTDisplayRecipeCategory
import hiiragi283.core.api.integration.jei.setFluidSlotRenderer
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.api.recipe.viewer.display.HTRecipeDisplay
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HCTankEmptyingRecipeCategory(guiHelper: IGuiHelper) : HTDisplayRecipeCategory.Simple(guiHelper, HCRecipeViewerTypes.EMPTYING) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addItemStacks(contents.inputItem(0))
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .addFluidStack(contents.outputFluid(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
            .setFluidSlotRenderer()
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .addChancedItem(contents.outputItem(0))
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTRecipeDisplay.Simple, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(1.25), getPosition(0))
        builder.addRecipePlus(getPosition(4))
    }
}
