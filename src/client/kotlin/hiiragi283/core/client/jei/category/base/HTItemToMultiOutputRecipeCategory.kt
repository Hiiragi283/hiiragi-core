package hiiragi283.core.client.jei.category.base

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.HTItemToMultiOutputRecipe
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.viewer.HTLookupRecipeViewerType
import hiiragi283.core.client.jei.extension.HTItemToMultiOutputRecipeCategoryExtension
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

abstract class HTItemToMultiOutputRecipeCategory(
    guiHelper: IGuiHelper,
    recipeType: HTLookupRecipeViewerType<*, HTItemToMultiOutputRecipe>,
    private val maxOutputs: Int,
) : HTLookupRecipeCategory<HTItemToMultiOutputRecipe>(guiHelper, recipeType) {
    private val extensions: MutableMap<Class<out HTItemToMultiOutputRecipe>, HTItemToMultiOutputRecipeCategoryExtension<*>> = hashMapOf()

    inline fun <reified RECIPE : HTItemToMultiOutputRecipe> addExtension(extension: HTItemToMultiOutputRecipeCategoryExtension<RECIPE>) {
        this.addExtension(RECIPE::class.java, extension)
    }

    fun <RECIPE : HTItemToMultiOutputRecipe> addExtension(
        clazz: Class<RECIPE>,
        extension: HTItemToMultiOutputRecipeCategoryExtension<RECIPE>,
    ) {
        extensions[clazz] = extension
    }

    //    HTLookupRecipeCategory    //

    final override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTItemToMultiOutputRecipe, focuses: IFocusGroup) {
        val (recipe1: HTItemToMultiOutputRecipe, extension: HTItemToMultiOutputRecipeCategoryExtension<HTItemToMultiOutputRecipe>) =
            getExtension<HTItemToMultiOutputRecipe>(recipe) ?: return
        // input
        extension.setInput(
            recipe1,
            builder.addInputSlot(getPosition(0), getPosition(0.5)).setSlotBackground(HTBackgroundType.INPUT),
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

    override fun createRecipeExtrasImpl(builder: IRecipeExtrasBuilder, recipe: HTItemToMultiOutputRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(1.25), getPosition(0.5))
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HTRecipeHolder<HTItemToMultiOutputRecipe>,
        recipeSlots: List<IRecipeSlotDrawable?>,
        focuses: IFocusGroup,
    ) {
        val (recipe1: HTItemToMultiOutputRecipe, extension: HTItemToMultiOutputRecipeCategoryExtension<HTItemToMultiOutputRecipe>) =
            getExtension<HTItemToMultiOutputRecipe>(recipe.recipe) ?: return
    }

    override fun isHandled(recipe: HTRecipeHolder<HTItemToMultiOutputRecipe>): Boolean =
        getExtension<HTItemToMultiOutputRecipe>(recipe.recipe) != null

    @Suppress("UNCHECKED_CAST")
    private fun <RECIPE : HTItemToMultiOutputRecipe> getExtension(
        recipe: HTItemToMultiOutputRecipe,
    ): Pair<RECIPE, HTItemToMultiOutputRecipeCategoryExtension<RECIPE>>? {
        val extension: HTItemToMultiOutputRecipeCategoryExtension<RECIPE> =
            (extensions[recipe::class.java] as? HTItemToMultiOutputRecipeCategoryExtension<RECIPE>) ?: run {
                for ((clazz: Class<out HTItemToMultiOutputRecipe>, extension: HTItemToMultiOutputRecipeCategoryExtension<*>) in extensions) {
                    if (clazz.isInstance(recipe)) {
                        return@run extension as? HTItemToMultiOutputRecipeCategoryExtension<RECIPE>
                    }
                }
                null
            } ?: return null
        return (recipe as RECIPE) to extension
    }
}
