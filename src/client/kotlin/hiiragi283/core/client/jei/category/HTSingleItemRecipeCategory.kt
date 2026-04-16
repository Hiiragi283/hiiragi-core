package hiiragi283.core.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.core.api.recipe.viewer.HTLookupRecipeViewerType
import hiiragi283.core.impl.recipe.HTBasicSingleItemRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeSerializer

open class HTSingleItemRecipeCategory<RECIPE : HTBasicSingleItemRecipe>(
    guiHelper: IGuiHelper,
    recipeType: HTLookupRecipeViewerType<*, RECIPE>,
    serializer: RecipeSerializer<RECIPE>,
) : HTHolderRecipeCategory.Registered<RECIPE>(guiHelper, recipeType, serializer) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addItemIngredient(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .addItemResult(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(1.25), getPosition(0))
    }
}
