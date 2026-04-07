package hiiragi283.core.client.jei.category.base

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.HTDoubleItemToMultiOutputRecipe
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.viewer.HTLookupRecipeViewerType
import hiiragi283.core.client.jei.extension.HTDoubleItemToMultiOutputRecipeCategoryExtension
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

abstract class HTDoubleItemToMultiOutputRecipeCategory(
    guiHelper: IGuiHelper,
    recipeType: HTLookupRecipeViewerType<*, HTDoubleItemToMultiOutputRecipe>,
    private val maxOutputs: Int,
) : HTLookupRecipeCategory<HTDoubleItemToMultiOutputRecipe>(guiHelper, recipeType) {
    private val extensions: MutableMap<
        Class<out HTDoubleItemToMultiOutputRecipe>,
        HTDoubleItemToMultiOutputRecipeCategoryExtension<*>,
    > = hashMapOf()

    inline fun <reified RECIPE : HTDoubleItemToMultiOutputRecipe> addExtension(
        extension: HTDoubleItemToMultiOutputRecipeCategoryExtension<RECIPE>,
    ) {
        this.addExtension(RECIPE::class.java, extension)
    }

    fun <RECIPE : HTDoubleItemToMultiOutputRecipe> addExtension(
        clazz: Class<RECIPE>,
        extension: HTDoubleItemToMultiOutputRecipeCategoryExtension<RECIPE>,
    ) {
        extensions[clazz] = extension
    }

    //    HTLookupRecipeCategory    //

    final override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTDoubleItemToMultiOutputRecipe, focuses: IFocusGroup) {
        val (
            recipe1: HTDoubleItemToMultiOutputRecipe,
            extension: HTDoubleItemToMultiOutputRecipeCategoryExtension<HTDoubleItemToMultiOutputRecipe>,
        ) = getExtension<HTDoubleItemToMultiOutputRecipe>(recipe) ?: return
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

    override fun createRecipeExtrasImpl(builder: IRecipeExtrasBuilder, recipe: HTDoubleItemToMultiOutputRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(1.25), getPosition(0.5))
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HTRecipeHolder<HTDoubleItemToMultiOutputRecipe>,
        recipeSlots: List<IRecipeSlotDrawable?>,
        focuses: IFocusGroup,
    ) {
        val (
            recipe1: HTDoubleItemToMultiOutputRecipe,
            extension: HTDoubleItemToMultiOutputRecipeCategoryExtension<HTDoubleItemToMultiOutputRecipe>,
        ) = getExtension<HTDoubleItemToMultiOutputRecipe>(recipe.recipe) ?: return
    }

    override fun isHandled(recipe: HTRecipeHolder<HTDoubleItemToMultiOutputRecipe>): Boolean =
        getExtension<HTDoubleItemToMultiOutputRecipe>(recipe.recipe) != null

    @Suppress("UNCHECKED_CAST")
    private fun <RECIPE : HTDoubleItemToMultiOutputRecipe> getExtension(
        recipe: HTDoubleItemToMultiOutputRecipe,
    ): Pair<RECIPE, HTDoubleItemToMultiOutputRecipeCategoryExtension<RECIPE>>? {
        val extension: HTDoubleItemToMultiOutputRecipeCategoryExtension<RECIPE> =
            (extensions[recipe::class.java] as? HTDoubleItemToMultiOutputRecipeCategoryExtension<RECIPE>) ?: run {
                for ((
                    clazz: Class<out HTDoubleItemToMultiOutputRecipe>,
                    extension: HTDoubleItemToMultiOutputRecipeCategoryExtension<*>,
                ) in extensions) {
                    if (clazz.isInstance(recipe)) {
                        return@run extension as? HTDoubleItemToMultiOutputRecipeCategoryExtension<RECIPE>
                    }
                }
                null
            } ?: return null
        return (recipe as RECIPE) to extension
    }
}
