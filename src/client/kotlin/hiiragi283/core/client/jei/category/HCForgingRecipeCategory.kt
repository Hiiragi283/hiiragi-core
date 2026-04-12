package hiiragi283.core.client.jei.category

import hiiragi283.core.client.jei.category.base.HTDoubleMultiOutputRecipeCategory
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import mezz.jei.api.helpers.IGuiHelper

class HCForgingRecipeCategory(guiHelper: IGuiHelper) : HTDoubleMultiOutputRecipeCategory(guiHelper, HCRecipeViewerTypes.FORGING, 9) {
    override fun getOutputPos(index: Int): Pair<Int, Int> = getPosition(3 + index % 3) to getPosition(0 + index / 3)
}
