package hiiragi283.core.client.integration.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.add
import hiiragi283.core.api.recipe.FakeRecipeHolder
import hiiragi283.core.client.integration.jei.HCJeiRecipeTypes
import hiiragi283.core.client.integration.jei.category.base.HTLookupRecipeCategory
import hiiragi283.core.common.recipe.HCBrewingRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HCBrewingRecipeCategory(guiHelper: IGuiHelper) : HTLookupRecipeCategory.Fake<HCBrewingRecipe>(guiHelper, HCJeiRecipeTypes.BREWING) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HCBrewingRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.potionFrom)
            .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .add(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .add(recipe.potionTo)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: FakeRecipeHolder<HCBrewingRecipe>, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.recipe.time).setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(1))
    }
}
