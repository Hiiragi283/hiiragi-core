package hiiragi283.core.client.jei.category.base

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.HTRecipeHolder
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
    private val maxOutputs: Int,
) : HTLookupRecipeCategory<HTDoubleMultiOutputRecipe>(guiHelper, recipeType) {
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
        val (
            recipe1: HTDoubleMultiOutputRecipe,
            extension: HTDoubleMultiOutputRecipeCategoryExtension<HTDoubleMultiOutputRecipe>,
        ) = getExtension<HTDoubleMultiOutputRecipe>(recipe) ?: return
        // inputs
        extension.setBase(
            recipe1,
            builder.addInputSlot(getPosition(0), getPosition(0)).setSlotBackground(HTBackgroundType.INPUT),
        )
        extension.setBase(
            recipe1,
            builder.addInputSlot(getPosition(0), getPosition(2)).setSlotBackground(HTBackgroundType.EXTRA_INPUT),
        )
        // outputs
        Array(maxOutputs) { index: Int ->
            val (x: Int, y: Int) = getOutputPos(index)
            builder.addOutputSlot(x, y).setSlotBackground(HTBackgroundType.OUTPUT)
        }.forEachIndexed { index: Int, builder: IRecipeSlotBuilder ->
            extension.setOutput(recipe1, index, builder)
        }
    }

    protected abstract fun getOutputPos(index: Int): Pair<Int, Int>

    override fun createRecipeExtrasImpl(builder: IRecipeExtrasBuilder, recipe: HTDoubleMultiOutputRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(1.25), getPosition(0.5))
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HTRecipeHolder<HTDoubleMultiOutputRecipe>,
        recipeSlots: List<IRecipeSlotDrawable?>,
        focuses: IFocusGroup,
    ) {
        val (
            recipe1: HTDoubleMultiOutputRecipe,
            extension: HTDoubleMultiOutputRecipeCategoryExtension<HTDoubleMultiOutputRecipe>,
        ) = getExtension<HTDoubleMultiOutputRecipe>(recipe.recipe) ?: return
    }

    override fun isHandled(recipe: HTRecipeHolder<HTDoubleMultiOutputRecipe>): Boolean =
        getExtension<HTDoubleMultiOutputRecipe>(recipe.recipe) != null

    @Suppress("UNCHECKED_CAST")
    private fun <RECIPE : HTDoubleMultiOutputRecipe> getExtension(
        recipe: HTDoubleMultiOutputRecipe,
    ): Pair<RECIPE, HTDoubleMultiOutputRecipeCategoryExtension<RECIPE>>? {
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
        return (recipe as RECIPE) to extension
    }
}
