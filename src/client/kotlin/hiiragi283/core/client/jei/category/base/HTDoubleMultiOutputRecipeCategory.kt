package hiiragi283.core.client.jei.category.base

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.core.impl.recipe.HTBasicDoubleMultiOutputRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeSerializer
import kotlin.jvm.optionals.getOrNull

abstract class HTDoubleMultiOutputRecipeCategory<RECIPE : HTBasicDoubleMultiOutputRecipe>(
    guiHelper: IGuiHelper,
    recipeType: HTHolderRecipeViewerType<RECIPE>,
    serializer: RecipeSerializer<RECIPE>,
) : HTMultiOutputRecipeCategory<RECIPE>(guiHelper, recipeType, serializer) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0.5))
            .addItemIngredient(recipe.base)
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(0), getPosition(2))
            .addItemIngredient(recipe.addition.getOrNull())
            .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
        // outputs
        setupOutputs(builder, recipe, focuses)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(1.25), getPosition(1))
    }
}
