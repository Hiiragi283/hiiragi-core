package hiiragi283.core.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addChancedItem
import hiiragi283.core.api.integration.jei.category.HTDisplayRecipeCategory
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import hiiragi283.core.common.recipe.viewer.display.HCExplodingRecipeDisplay
import hiiragi283.core.common.text.HCTranslation
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.placement.HorizontalAlignment
import mezz.jei.api.gui.placement.VerticalAlignment
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HCExplodingRecipeCategory(guiHelper: IGuiHelper) :
    HTDisplayRecipeCategory<HCExplodingRecipeDisplay>(guiHelper, HCRecipeViewerTypes.EXPLODING, HCExplodingRecipeDisplay.CODEC) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HCExplodingRecipeDisplay, focuses: IFocusGroup) {
        val contents: HTRecipeContents = recipe.contents
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addItemStacks(contents.inputItem(0))
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .addChancedItem(contents.outputItem(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HCExplodingRecipeDisplay, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(1.25), getPosition(0))

        builder
            .addText(HCTranslation.EXPLOSION_POWER.translate(recipe.requiredPower), width, 10)
            .setPosition(0, 0, width, height, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM)
            .setTextAlignment(HorizontalAlignment.CENTER)
            .setTextAlignment(VerticalAlignment.BOTTOM)
            .setColor(0x808080)
    }
}
