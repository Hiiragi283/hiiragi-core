package hiiragi283.core.client.jei.category.base

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.base.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.viewer.HTLookupRecipeViewerType
import hiiragi283.core.client.jei.extension.HTDoubleMultiOutputRecipeCategoryExtension
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

abstract class HTDoubleMultiOutputRecipeCategory(
    guiHelper: IGuiHelper,
    recipeType: HTLookupRecipeViewerType<*, HTDoubleMultiOutputRecipe>,
    maxOutputs: Int,
) : HTMultiOutputRecipeCategory<HTDoubleMultiOutputRecipe>(guiHelper, recipeType, maxOutputs) {
    private val extensions: MutableMap<
        Class<out HTDoubleMultiOutputRecipe>,
        HTDoubleMultiOutputRecipeCategoryExtension<*>,
    > = hashMapOf()

    inline fun <reified RECIPE : HTDoubleMultiOutputRecipe> addExtension(extension: HTDoubleMultiOutputRecipeCategoryExtension<RECIPE>) {
        this.addExtension(RECIPE::class.java, extension)
    }

    fun <RECIPE : HTDoubleMultiOutputRecipe> addExtension(
        clazz: Class<RECIPE>,
        extension: HTDoubleMultiOutputRecipeCategoryExtension<RECIPE>,
    ) {
        extensions[clazz] = extension
    }

    //    HTLookupRecipeCategory    //

    final override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTDoubleMultiOutputRecipe, focuses: IFocusGroup) {
        val extension: HTDoubleMultiOutputRecipeCategoryExtension<HTDoubleMultiOutputRecipe> = getExtension(recipe) ?: return
        // inputs
        extension.setBase(
            recipe,
            builder.addInputSlot(getPosition(0), getPosition(0)).setSlotBackground(HTBackgroundType.INPUT),
        )
        extension.setAddition(
            recipe,
            builder.addInputSlot(getPosition(0), getPosition(2)).setSlotBackground(HTBackgroundType.EXTRA_INPUT),
        )
        // outputs
        addOutputSlots(builder) { index: Int, builder: IRecipeSlotBuilder ->
            extension.setOutput(recipe, index, builder)
        }
    }

    override fun createRecipeExtrasImpl(builder: IRecipeExtrasBuilder, recipe: HTDoubleMultiOutputRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(1.25), getPosition(0.5))
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HTRecipeHolder<HTDoubleMultiOutputRecipe>,
        recipeSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {
        val extension: HTDoubleMultiOutputRecipeCategoryExtension<HTDoubleMultiOutputRecipe> = getExtension(recipe.recipe) ?: return
        extension.onDisplayedIngredientsUpdate(
            recipe.recipe,
            recipeSlots[0],
            recipeSlots[1],
            recipeSlots.subList(2, maxOutputs + 1),
            focuses,
        )
    }

    override fun isHandled(recipe: HTRecipeHolder<HTDoubleMultiOutputRecipe>): Boolean =
        getExtension<HTDoubleMultiOutputRecipe>(recipe.recipe) != null

    @Suppress("UNCHECKED_CAST")
    private fun <RECIPE : HTDoubleMultiOutputRecipe> getExtension(
        recipe: HTDoubleMultiOutputRecipe,
    ): HTDoubleMultiOutputRecipeCategoryExtension<RECIPE>? {
        val extension: HTDoubleMultiOutputRecipeCategoryExtension<RECIPE> =
            (extensions[recipe::class.java] as? HTDoubleMultiOutputRecipeCategoryExtension<RECIPE>) ?: run {
                for ((
                    clazz: Class<out HTDoubleMultiOutputRecipe>,
                    extension: HTDoubleMultiOutputRecipeCategoryExtension<*>,
                ) in extensions) {
                    if (clazz.isInstance(recipe)) {
                        return@run extension as? HTDoubleMultiOutputRecipeCategoryExtension<RECIPE>
                    }
                }
                null
            } ?: return null
        return extension
    }
}
