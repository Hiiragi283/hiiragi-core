package hiiragi283.core.client.jei

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.HTHolderRecipeCategory
import hiiragi283.core.common.recipe.HCAnvilCrushingRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HCAnvilCrushingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory<HCAnvilCrushingRecipe>(guiHelper, HCJeiRecipeTypes.ANVIL_CRUSHING) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HCAnvilCrushingRecipe, focuses: IFocusGroup) {
        // input
        builder
            .addItemSlot(getPosition(0), getPosition(1), recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)

        // outputs
        builder
            .addItemSlot(getPosition(3), getPosition(1), recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
        // builder.addItemSlot(getPosition(3.5), getPosition(0), recipe.extraResult)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HCAnvilCrushingRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(1.25), getPosition(1))
    }
}
