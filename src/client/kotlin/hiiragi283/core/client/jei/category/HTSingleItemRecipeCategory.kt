package hiiragi283.core.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.HTSingleItemRecipe
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.viewer.HTLookupRecipeViewerType
import hiiragi283.core.client.jei.category.base.HTLookupRecipeCategory
import hiiragi283.core.client.jei.extension.HTSingleItemRecipeCategoryExtension
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTSingleItemRecipeCategory(guiHelper: IGuiHelper, recipeType: HTLookupRecipeViewerType<*, HTSingleItemRecipe>) :
    HTLookupRecipeCategory<HTSingleItemRecipe>(guiHelper, recipeType) {
    private val extensions: MutableMap<Class<out HTSingleItemRecipe>, HTSingleItemRecipeCategoryExtension<*>> = hashMapOf()

    inline fun <reified RECIPE : HTSingleItemRecipe> addExtension(extension: HTSingleItemRecipeCategoryExtension<RECIPE>) {
        this.addExtension(RECIPE::class.java, extension)
    }

    fun <RECIPE : HTSingleItemRecipe> addExtension(clazz: Class<RECIPE>, extension: HTSingleItemRecipeCategoryExtension<RECIPE>) {
        extensions[clazz] = extension
    }

    //    HTLookupRecipeCategory    //

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTSingleItemRecipe, focuses: IFocusGroup) {
        val (recipe1: HTSingleItemRecipe, extension: HTSingleItemRecipeCategoryExtension<HTSingleItemRecipe>) =
            getExtension<HTSingleItemRecipe>(recipe) ?: return
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

    override fun createRecipeExtrasImpl(builder: IRecipeExtrasBuilder, recipe: HTSingleItemRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(1.25), getPosition(0))
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HTRecipeHolder<HTSingleItemRecipe>,
        recipeSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {
        val (recipe1: HTSingleItemRecipe, extension: HTSingleItemRecipeCategoryExtension<HTSingleItemRecipe>) =
            getExtension<HTSingleItemRecipe>(recipe.recipe) ?: return
        extension.onDisplayedIngredientsUpdate(recipe1, recipeSlots[0], recipeSlots[1], focuses)
    }

    override fun isHandled(recipe: HTRecipeHolder<HTSingleItemRecipe>): Boolean = getExtension<HTSingleItemRecipe>(recipe.recipe) != null

    @Suppress("UNCHECKED_CAST")
    private fun <RECIPE : HTSingleItemRecipe> getExtension(
        recipe: HTSingleItemRecipe,
    ): Pair<RECIPE, HTSingleItemRecipeCategoryExtension<RECIPE>>? {
        val extension: HTSingleItemRecipeCategoryExtension<RECIPE> =
            (extensions[recipe::class.java] as? HTSingleItemRecipeCategoryExtension<RECIPE>) ?: run {
                for ((clazz: Class<out HTSingleItemRecipe>, extension: HTSingleItemRecipeCategoryExtension<*>) in extensions) {
                    if (clazz.isInstance(recipe)) {
                        return@run extension as? HTSingleItemRecipeCategoryExtension<RECIPE>
                    }
                }
                null
            } ?: return null
        return (recipe as RECIPE) to extension
    }
}
