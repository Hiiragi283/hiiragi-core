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

class HCTankEmptyingRecipeCategory(guiHelper: IGuiHelper) : HTDisplayRecipeCategory.Simple(guiHelper, HCRecipeViewerTypes.EMPTYING) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addItemStacks(contents.inputItem(0))
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        contents.outputFluid(0) {
            builder
                .addOutputSlot(getPosition(3), getPosition(0))
                .add(it)
                .setSlotBackground(HTBackgroundType.OUTPUT, it.amount)
        }
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .add(contents.outputItem(0))
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTRecipeDisplay.Simple, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(1.25), getPosition(0))
        builder.addRecipePlus(getPosition(4))
    }
}
