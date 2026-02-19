package hiiragi283.core.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.client.jei.HCJeiRecipeTypes
import hiiragi283.core.common.recipe.HCAnvilCrushingRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeHolder

class HCAnvilCrushingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory<HCAnvilCrushingRecipe>(guiHelper, HCJeiRecipeTypes.ANVIL_CRUSHING) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HCAnvilCrushingRecipe, focuses: IFocusGroup) {
        // input
        builder
            .addItemSlot(getPosition(0), getPosition(0), recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)

        // outputs
        builder
            .addItemSlot(getPosition(3), getPosition(0), recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addItemSlot(getPosition(4), getPosition(0), recipe.extraResult)
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<HCAnvilCrushingRecipe>, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(1.25), getPosition(0))
    }
}
