package hiiragi283.core.client.jei.category.base

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.api.recipe.viewer.HTLookupRecipeViewerType
import hiiragi283.core.client.jei.extension.HTSingleMultiOutputRecipeCategoryExtension
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

abstract class HTSingleMultiOutputRecipeCategory(
    guiHelper: IGuiHelper,
    recipeType: HTLookupRecipeViewerType<*, HTSingleMultiOutputRecipe>,
    maxOutputs: Int,
) : HTMultiOutputRecipeCategory<HTSingleMultiOutputRecipe>(guiHelper, recipeType, maxOutputs) {
    private val extensions: MutableMap<Class<out HTSingleMultiOutputRecipe>, HTSingleMultiOutputRecipeCategoryExtension<*>> = hashMapOf()

    inline fun <reified RECIPE : HTSingleMultiOutputRecipe> addExtension(extension: HTSingleMultiOutputRecipeCategoryExtension<RECIPE>) {
        this.addExtension(RECIPE::class.java, extension)
    }

    fun <RECIPE : HTSingleMultiOutputRecipe> addExtension(
        clazz: Class<RECIPE>,
        extension: HTSingleMultiOutputRecipeCategoryExtension<RECIPE>,
    ) {
        extensions[clazz] = extension
    }

    //    HTLookupRecipeCategory    //

    final override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTSingleMultiOutputRecipe, focuses: IFocusGroup) {
        val extension: HTSingleMultiOutputRecipeCategoryExtension<HTSingleMultiOutputRecipe> = getExtension(recipe) ?: return
        // input
        extension.setInput(
            recipe,
            builder.addInputSlot(getPosition(0), getPosition(0.5)).setSlotBackground(HTBackgroundType.INPUT),
        )
        // outputs
        addOutputSlots(builder) { index: Int, builder: IRecipeSlotBuilder ->
            extension.setOutput(recipe, index, builder)
        }
    }

    override fun createRecipeExtrasImpl(builder: IRecipeExtrasBuilder, recipe: HTSingleMultiOutputRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(1.25), getPosition(0.5))
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HTRecipeHolder<HTSingleMultiOutputRecipe>,
        recipeSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {
        val extension: HTSingleMultiOutputRecipeCategoryExtension<HTSingleMultiOutputRecipe> = getExtension(recipe.recipe) ?: return
        extension.onDisplayedIngredientsUpdate(recipe.recipe, recipeSlots[0], recipeSlots.subList(1, maxOutputs + 1), focuses)
    }

    override fun isHandled(recipe: HTRecipeHolder<HTSingleMultiOutputRecipe>): Boolean =
        getExtension<HTSingleMultiOutputRecipe>(recipe.recipe) != null

    @Suppress("UNCHECKED_CAST")
    private fun <RECIPE : HTSingleMultiOutputRecipe> getExtension(
        recipe: HTSingleMultiOutputRecipe,
    ): HTSingleMultiOutputRecipeCategoryExtension<RECIPE>? {
        val extension: HTSingleMultiOutputRecipeCategoryExtension<RECIPE> =
            (extensions[recipe::class.java] as? HTSingleMultiOutputRecipeCategoryExtension<RECIPE>) ?: run {
                for ((
                    clazz: Class<out HTSingleMultiOutputRecipe>,
                    extension: HTSingleMultiOutputRecipeCategoryExtension<*>,
                ) in extensions) {
                    if (clazz.isInstance(recipe)) {
                        return@run extension as? HTSingleMultiOutputRecipeCategoryExtension<RECIPE>
                    }
                }
                null
            } ?: return null
        return extension
    }
}
