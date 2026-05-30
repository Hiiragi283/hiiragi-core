package hiiragi283.core.client.integration.jei.category

import hiiragi283.core.setup.HCRecipeViewerTypes
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.integration.jei.category.HTDisplayRecipeCategory
import hiiragi283.lib.recipe.viewer.display.HTRecipeContents
import hiiragi283.lib.recipe.viewer.display.HTRecipeDisplay
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HCTankFillingRecipeCategory(guiHelper: IGuiHelper) : HTDisplayRecipeCategory.Simple(guiHelper, HCRecipeViewerTypes.FILLING) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addItemStacks(contents.inputItem(0))
            .setSlotBackground(HTBackgroundType.INPUT)
        contents.inputFluid(0) {
            builder
                .addInputSlot(getPosition(2), getPosition(0))
                .add(it.stacks)
                .setSlotBackground(HTBackgroundType.INPUT, it.capacity)
        }
        // output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .add(contents.outputItem(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTRecipeDisplay.Simple, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(1))
    }
}
