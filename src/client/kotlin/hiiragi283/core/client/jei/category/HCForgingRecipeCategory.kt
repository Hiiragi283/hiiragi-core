package hiiragi283.core.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addChancedItem
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.client.jei.category.base.HTDoubleMultiOutputRecipeCategory
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HCForgingRecipeCategory(guiHelper: IGuiHelper) : HTDoubleMultiOutputRecipeCategory(guiHelper, HCRecipeViewerTypes.FORGING) {
    override fun setupOutputs(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        repeat(9) { index: Int ->
            builder
                .addOutputSlot(getPosition(3 + index % 3), getPosition(0 + index / 3))
                .addChancedItem(contents.outputItem(index))
                .setSlotBackground(HTBackgroundType.OUTPUT)
        }
    }
}
