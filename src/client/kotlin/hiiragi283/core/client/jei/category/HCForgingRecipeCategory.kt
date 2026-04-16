package hiiragi283.core.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.client.jei.category.base.HTDoubleMultiOutputRecipeCategory
import hiiragi283.core.common.recipe.HCForgingRecipe
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import hiiragi283.core.setup.HCRecipeSerializers
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HCForgingRecipeCategory(guiHelper: IGuiHelper) :
    HTDoubleMultiOutputRecipeCategory<HCForgingRecipe>(guiHelper, HCRecipeViewerTypes.FORGING, HCRecipeSerializers.FORGING) {
    override fun setupOutputs(builder: IRecipeLayoutBuilder, recipe: HCForgingRecipe, focuses: IFocusGroup) {
        repeat(9) { index: Int ->
            builder
                .addOutputSlot(getPosition(3 + index % 3), getPosition(0 + index / 3))
                .addItemResult(recipe.results.getOrNull(index))
                .setSlotBackground(HTBackgroundType.OUTPUT)
        }
    }
}
