package hiiragi283.core.client.jei.category.base

import hiiragi283.core.api.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.core.api.recipe.base.HTMultiOutputRecipe
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeSerializer

abstract class HTMultiOutputRecipeCategory<RECIPE : HTMultiOutputRecipe.Serializable<*>>(
    guiHelper: IGuiHelper,
    recipeType: HTHolderRecipeViewerType<RECIPE>,
    serializer: RecipeSerializer<RECIPE>,
) : HTHolderRecipeCategory.Registered<RECIPE>(guiHelper, recipeType, serializer) {
    protected abstract fun setupOutputs(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup)
}
