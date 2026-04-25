package hiiragi283.core.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import hiiragi283.core.common.text.HCTranslation
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.placement.HorizontalAlignment
import mezz.jei.api.gui.placement.VerticalAlignment
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HCExplodingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory.Registered<HCExplodingRecipe>(guiHelper, HCRecipeViewerTypes.EXPLODING, HCExplodingRecipe.CODEC) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HCExplodingRecipe, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addIngredients(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .addItemResult(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HCExplodingRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(1.25), getPosition(0))

        builder
            .addText(HCTranslation.EXPLOSION_POWER.translate(recipe.requiredPower), width, 10)
            .setPosition(0, 0, width, height, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM)
            .setTextAlignment(HorizontalAlignment.CENTER)
            .setTextAlignment(VerticalAlignment.BOTTOM)
            .setColor(0x808080)
    }
}
