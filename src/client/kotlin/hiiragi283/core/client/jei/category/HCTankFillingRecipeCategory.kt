package hiiragi283.core.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import hiiragi283.core.setup.HCRecipeSerializers
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HCTankFillingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory.Registered<HCTankFillingRecipe>(guiHelper, HCRecipeViewerTypes.FILLING, HCRecipeSerializers.FILLING) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HCTankFillingRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addIngredients(recipe.itemIngredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .addFluidIngredient(recipe.fluidIngredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .addItemResult(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HCTankFillingRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(1))
    }
}
