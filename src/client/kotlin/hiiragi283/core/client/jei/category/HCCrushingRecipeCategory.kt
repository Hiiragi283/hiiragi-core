package hiiragi283.core.client.jei.category

import hiiragi283.core.client.jei.HCJeiRecipeTypes
import hiiragi283.core.client.jei.category.base.HTSingleMultiOutputRecipeCategory
import mezz.jei.api.helpers.IGuiHelper

class HCCrushingRecipeCategory(guiHelper: IGuiHelper) : HTSingleMultiOutputRecipeCategory(guiHelper, HCJeiRecipeTypes.CRUSHING, 4) {
    override fun getOutputPos(index: Int): Pair<Int, Int> = getPosition(3 + index % 2) to getPosition(0 + index / 2)
}
