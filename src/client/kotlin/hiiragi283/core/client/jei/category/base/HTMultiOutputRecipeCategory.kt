package hiiragi283.core.client.jei.category.base

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.base.HTMultiOutputRecipe
import hiiragi283.core.api.recipe.viewer.HTLookupRecipeViewerType
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.helpers.IGuiHelper

abstract class HTMultiOutputRecipeCategory<RECIPE : HTMultiOutputRecipe<*>>(
    guiHelper: IGuiHelper,
    recipeType: HTLookupRecipeViewerType<*, RECIPE>,
    protected val maxOutputs: Int,
) : HTLookupRecipeCategory<RECIPE>(guiHelper, recipeType) {
    protected inline fun addOutputSlots(builder: IRecipeLayoutBuilder, action: (Int, IRecipeSlotBuilder) -> Unit) {
        Array(maxOutputs) { index: Int ->
            val (x: Int, y: Int) = getOutputPos(index)
            builder.addOutputSlot(x, y).setSlotBackground(HTBackgroundType.OUTPUT)
        }.forEachIndexed(action)
    }

    protected abstract fun getOutputPos(index: Int): Pair<Int, Int>
}
