package hiiragi283.core.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.HTItemToItemRecipe
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.viewer.HTLookupRecipeViewerType
import hiiragi283.core.client.jei.category.base.HTLookupRecipeCategory
import hiiragi283.core.client.jei.extension.HTItemToItemRecipeCategoryExtension
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTItemToItemRecipeCategory(guiHelper: IGuiHelper, recipeType: HTLookupRecipeViewerType<*, HTItemToItemRecipe>) :
    HTLookupRecipeCategory<HTItemToItemRecipe>(guiHelper, recipeType) {
    private val extensions: MutableMap<Class<out HTItemToItemRecipe>, HTItemToItemRecipeCategoryExtension<*>> = hashMapOf()

    inline fun <reified RECIPE : HTItemToItemRecipe> addExtension(extension: HTItemToItemRecipeCategoryExtension<RECIPE>) {
        this.addExtension(RECIPE::class.java, extension)
    }

    fun <RECIPE : HTItemToItemRecipe> addExtension(clazz: Class<RECIPE>, extension: HTItemToItemRecipeCategoryExtension<RECIPE>) {
        extensions[clazz] = extension
    }

    //    HTProcessingRecipeCategory    //

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTItemToItemRecipe, focuses: IFocusGroup) {
        val (recipe1: HTItemToItemRecipe, extension: HTItemToItemRecipeCategoryExtension<HTItemToItemRecipe>) =
            getExtension<HTItemToItemRecipe>(recipe) ?: return
        // input
        extension.setInput(
            recipe1,
            builder.addInputSlot(getPosition(0), getPosition(0)).setSlotBackground(HTBackgroundType.INPUT),
        )
        // output
        extension.setOutput(
            recipe1,
            builder.addOutputSlot(getPosition(3), getPosition(0)).setSlotBackground(HTBackgroundType.OUTPUT),
        )
    }

    override fun createRecipeExtrasImpl(builder: IRecipeExtrasBuilder, recipe: HTItemToItemRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(1.25), getPosition(0))
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HTRecipeHolder<HTItemToItemRecipe>,
        recipeSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {
        val (recipe1: HTItemToItemRecipe, extension: HTItemToItemRecipeCategoryExtension<HTItemToItemRecipe>) =
            getExtension<HTItemToItemRecipe>(recipe.recipe) ?: return
        extension.onDisplayedIngredientsUpdate(recipe1, recipeSlots[0], recipeSlots[1], focuses)
    }

    override fun isHandled(recipe: HTRecipeHolder<HTItemToItemRecipe>): Boolean = getExtension<HTItemToItemRecipe>(recipe.recipe) != null

    @Suppress("UNCHECKED_CAST")
    private fun <RECIPE : HTItemToItemRecipe> getExtension(
        recipe: HTItemToItemRecipe,
    ): Pair<RECIPE, HTItemToItemRecipeCategoryExtension<RECIPE>>? {
        val extension: HTItemToItemRecipeCategoryExtension<RECIPE> =
            (extensions[recipe::class.java] as? HTItemToItemRecipeCategoryExtension<RECIPE>) ?: run {
                for ((clazz: Class<out HTItemToItemRecipe>, extension: HTItemToItemRecipeCategoryExtension<*>) in extensions) {
                    if (clazz.isInstance(recipe)) {
                        return@run extension as? HTItemToItemRecipeCategoryExtension<RECIPE>
                    }
                }
                null
            } ?: return null
        return (recipe as RECIPE) to extension
    }
}
