package hiiragi283.core.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidResult
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.core.common.recipe.HCMeltingRecipe
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import hiiragi283.core.setup.HCRecipeSerializers
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HCMeltingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory.Registered<HCMeltingRecipe>(guiHelper, HCRecipeViewerTypes.MELTING, HCRecipeSerializers.MELTING) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HCMeltingRecipe, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addItemIngredient(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
            .addRichTooltipCallback { _, builder: ITooltipBuilder -> builder.add(recipe.heatRange.getText()) }
        // output
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .addFluidResult(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HCMeltingRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(1.25), getPosition(0))
    }
}
