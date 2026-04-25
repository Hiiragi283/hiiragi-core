package hiiragi283.core.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addChancedItem
import hiiragi283.core.api.integration.jei.category.HTDisplayRecipeCategory
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import hiiragi283.core.common.recipe.viewer.display.HCChargingRecipeDisplay
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.placement.HorizontalAlignment
import mezz.jei.api.gui.placement.VerticalAlignment
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HCChargingRecipeCategory(guiHelper: IGuiHelper) :
    HTDisplayRecipeCategory<HCChargingRecipeDisplay>(guiHelper, HCRecipeViewerTypes.CHARGING, HCChargingRecipeDisplay.CODEC) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HCChargingRecipeDisplay, focuses: IFocusGroup) {
        val contents = recipe.contents
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

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HCChargingRecipeDisplay, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(1.25), getPosition(0))

        builder
            .addText(HTCommonTranslation.STORED_FE.translate(recipe.requiredEnergy), width, 10)
            .setPosition(0, 0, width, height, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM)
            .setTextAlignment(HorizontalAlignment.CENTER)
            .setTextAlignment(VerticalAlignment.BOTTOM)
            .setColor(0x808080)
    }
}
