package hiiragi283.core.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.HTItemToChancedRecipe
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.viewer.HTLookupRecipeViewerType
import hiiragi283.core.client.jei.category.base.HTLookupRecipeCategory
import hiiragi283.core.client.jei.extension.HTItemToChancedRecipeCategoryExtension
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTItemToChancedRecipeCategory(guiHelper: IGuiHelper, recipeType: HTLookupRecipeViewerType<*, HTItemToChancedRecipe>) :
    HTLookupRecipeCategory<HTItemToChancedRecipe>(guiHelper, recipeType) {
    private val extensions: MutableMap<Class<out HTItemToChancedRecipe>, HTItemToChancedRecipeCategoryExtension<*>> = hashMapOf()

    inline fun <reified RECIPE : HTItemToChancedRecipe> addExtension(extension: HTItemToChancedRecipeCategoryExtension<RECIPE>) {
        this.addExtension(RECIPE::class.java, extension)
    }

    fun <RECIPE : HTItemToChancedRecipe> addExtension(clazz: Class<RECIPE>, extension: HTItemToChancedRecipeCategoryExtension<RECIPE>) {
        extensions[clazz] = extension
    }

    //    HTProcessingRecipeCategory    //

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTItemToChancedRecipe, focuses: IFocusGroup) {
        val (recipe1: HTItemToChancedRecipe, extension: HTItemToChancedRecipeCategoryExtension<HTItemToChancedRecipe>) =
            getExtension<HTItemToChancedRecipe>(recipe) ?: return
        // input
        extension.setInput(
            recipe1,
            builder.addInputSlot(getPosition(0), getPosition(0)).setSlotBackground(HTBackgroundType.INPUT),
        )
        // outputs
        extension.setOutput(
            recipe1,
            builder.addOutputSlot(getPosition(3), getPosition(0)).setSlotBackground(HTBackgroundType.OUTPUT),
        )
        extension.setExtraOutput(
            recipe1,
            builder.addOutputSlot(getPosition(5), getPosition(0)).setSlotBackground(HTBackgroundType.EXTRA_OUTPUT),
        )
    }

    override fun createRecipeExtrasImpl(builder: IRecipeExtrasBuilder, recipe: HTItemToChancedRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(1.25), getPosition(0))
        builder.addRecipePlus(getPosition(4))
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HTRecipeHolder<HTItemToChancedRecipe>,
        recipeSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {
        val (recipe1: HTItemToChancedRecipe, extension: HTItemToChancedRecipeCategoryExtension<HTItemToChancedRecipe>) =
            getExtension<HTItemToChancedRecipe>(recipe.recipe) ?: return
        extension.onDisplayedIngredientsUpdate(recipe1, recipeSlots[0], recipeSlots[1], recipeSlots[2], focuses)
    }

    override fun isHandled(recipe: HTRecipeHolder<HTItemToChancedRecipe>): Boolean =
        getExtension<HTItemToChancedRecipe>(recipe.recipe) != null

    @Suppress("UNCHECKED_CAST")
    private fun <RECIPE : HTItemToChancedRecipe> getExtension(
        recipe: HTItemToChancedRecipe,
    ): Pair<RECIPE, HTItemToChancedRecipeCategoryExtension<RECIPE>>? {
        val extension: HTItemToChancedRecipeCategoryExtension<RECIPE> =
            (extensions[recipe::class.java] as? HTItemToChancedRecipeCategoryExtension<RECIPE>) ?: run {
                for ((clazz: Class<out HTItemToChancedRecipe>, extension: HTItemToChancedRecipeCategoryExtension<*>) in extensions) {
                    if (clazz.isInstance(recipe)) {
                        return@run extension as? HTItemToChancedRecipeCategoryExtension<RECIPE>
                    }
                }
                null
            } ?: return null
        return (recipe as RECIPE) to extension
    }
}
