package hiiragi283.core.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.client.jei.category.base.HTSingleMultiOutputRecipeCategory
import hiiragi283.core.common.recipe.HCCrushingRecipe
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import hiiragi283.core.setup.HCRecipeSerializers
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HCCrushingRecipeCategory(guiHelper: IGuiHelper) :
    HTSingleMultiOutputRecipeCategory<HCCrushingRecipe>(guiHelper, HCRecipeViewerTypes.CRUSHING, HCRecipeSerializers.CRUSHING) {
    override fun setupOutputs(builder: IRecipeLayoutBuilder, recipe: HCCrushingRecipe, focuses: IFocusGroup) {
        repeat(4) { index: Int ->
            builder
                .addOutputSlot(getPosition(3 + index % 2), getPosition(0 + index / 2))
                .addItemResult(recipe.results.getOrNull(index))
                .setSlotBackground(HTBackgroundType.OUTPUT)
        }
    }
}
