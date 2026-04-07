package hiiragi283.core.client.jei.category

import hiiragi283.core.client.jei.HCJeiRecipeTypes
import hiiragi283.core.client.jei.category.base.HTDoubleMultiOutputRecipeCategory
import mezz.jei.api.helpers.IGuiHelper

class HCForgingRecipeCategory(guiHelper: IGuiHelper) : HTDoubleMultiOutputRecipeCategory(guiHelper, HCJeiRecipeTypes.FORGING, 9) {
    override fun getOutputPos(index: Int): Pair<Int, Int> = getPosition(3 + index % 3) to getPosition(0 + index / 3)
}
