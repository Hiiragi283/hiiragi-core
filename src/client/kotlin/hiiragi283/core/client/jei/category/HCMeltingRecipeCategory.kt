package hiiragi283.core.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidResult
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.client.jei.HCJeiRecipeTypes
import hiiragi283.core.client.jei.category.base.HTLookupRecipeCategory
import hiiragi283.core.common.recipe.HCMeltingRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeHolder

class HCMeltingRecipeCategory(guiHelper: IGuiHelper) :
    HTLookupRecipeCategory.Managed<HCMeltingRecipe>(guiHelper, HCJeiRecipeTypes.MELTING) {
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

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<HCMeltingRecipe>, focuses: IFocusGroup) {
        val recipe: HCMeltingRecipe = recipe.value()
        builder
            .addAnimatedRecipeArrow(recipe.time)
            .setPosition(getPosition(1.25), getPosition(0))
    }
}
