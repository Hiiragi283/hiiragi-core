package hiiragi283.core.client.integration.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.add
import hiiragi283.core.client.integration.jei.HCJeiRecipeTypes
import hiiragi283.core.client.integration.jei.category.base.HTLookupRecipeCategory
import hiiragi283.core.common.recipe.HCChargingRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeHolder

class HCChargingRecipeCategory(guiHelper: IGuiHelper) :
    HTLookupRecipeCategory.Managed<HCChargingRecipe>(guiHelper, HCJeiRecipeTypes.CHARGING) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HCChargingRecipe, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .add(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<HCChargingRecipe>, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(1.25), getPosition(0))
    }
}
