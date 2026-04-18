package hiiragi283.core.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidResult
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import hiiragi283.core.setup.HCRecipeSerializers
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import kotlin.jvm.optionals.getOrNull

class HCTankEmptyingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory.Registered<HCTankEmptyingRecipe>(guiHelper, HCRecipeViewerTypes.EMPTYING, HCRecipeSerializers.EMPTYING) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HCTankEmptyingRecipe, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addIngredients(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .addFluidResult(recipe.fluidResult)
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .addItemResult(recipe.itemResult.getOrNull())
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HCTankEmptyingRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(1.25), getPosition(0))
        builder.addRecipePlus(getPosition(4))
    }
}
