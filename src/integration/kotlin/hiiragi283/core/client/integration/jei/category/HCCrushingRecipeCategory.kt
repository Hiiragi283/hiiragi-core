package hiiragi283.core.client.integration.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addChancedItem
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.client.integration.jei.category.base.HTItemToMultiItemRecipeCategory
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HCCrushingRecipeCategory(guiHelper: IGuiHelper) : HTItemToMultiItemRecipeCategory(guiHelper, HCRecipeViewerTypes.CRUSHING) {
    override fun setupOutputs(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        repeat(4) { index: Int ->
            builder
                .addOutputSlot(getPosition(3 + index % 2), getPosition(0 + index / 2))
                .addChancedItem(contents.outputItem(index))
                .setSlotBackground(HTBackgroundType.OUTPUT)
        }
    }
}
