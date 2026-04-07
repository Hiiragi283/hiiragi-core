package hiiragi283.core.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.client.jei.HCJeiRecipeTypes
import hiiragi283.core.client.jei.category.base.HTLookupRecipeCategory
import hiiragi283.core.client.jei.extension.HTForgingRecipeCategoryExtension
import hiiragi283.core.common.recipe.HCForgingRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HCForgingRecipeCategory(guiHelper: IGuiHelper) : HTLookupRecipeCategory<HCForgingRecipe>(guiHelper, HCJeiRecipeTypes.FORGING) {
    private val extensions: MutableMap<Class<out HCForgingRecipe>, HTForgingRecipeCategoryExtension<*>> = hashMapOf()

    inline fun <reified RECIPE : HCForgingRecipe> addExtension(extension: HTForgingRecipeCategoryExtension<RECIPE>) {
        this.addExtension(RECIPE::class.java, extension)
    }

    fun <RECIPE : HCForgingRecipe> addExtension(clazz: Class<RECIPE>, extension: HTForgingRecipeCategoryExtension<RECIPE>) {
        extensions[clazz] = extension
    }

    //    HTLookupRecipeCategory    //

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HCForgingRecipe, focuses: IFocusGroup) {
        val (recipe1: HCForgingRecipe, extension: HTForgingRecipeCategoryExtension<HCForgingRecipe>) =
            getExtension<HCForgingRecipe>(recipe) ?: return
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
        Array(9) { index: Int ->
            builder
                .addOutputSlot(getPosition(3 + index % 3), getPosition(0 + index / 3))
                .setSlotBackground(HTBackgroundType.OUTPUT)
        }.forEachIndexed { index: Int, builder: IRecipeSlotBuilder -> extension.setOutput(recipe, index, builder) }
    }

    override fun createRecipeExtrasImpl(builder: IRecipeExtrasBuilder, recipe: HCForgingRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(1.25), getPosition(1))
        builder.addRecipePlus(getPosition(0), getPosition(1))
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HTRecipeHolder<HCForgingRecipe>,
        recipeSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {
        val (recipe1: HCForgingRecipe, extension: HTForgingRecipeCategoryExtension<HCForgingRecipe>) =
            getExtension<HCForgingRecipe>(recipe.recipe) ?: return
        extension.onDisplayedIngredientsUpdate(recipe1, recipeSlots[0], recipeSlots[1], { index: Int -> recipeSlots[index + 2] }, focuses)
    }

    override fun isHandled(recipe: HTRecipeHolder<HCForgingRecipe>): Boolean = getExtension<HCForgingRecipe>(recipe.recipe) != null

    @Suppress("UNCHECKED_CAST")
    private fun <RECIPE : HCForgingRecipe> getExtension(recipe: HCForgingRecipe): Pair<RECIPE, HTForgingRecipeCategoryExtension<RECIPE>>? {
        val extension: HTForgingRecipeCategoryExtension<RECIPE> =
            (extensions[recipe::class.java] as? HTForgingRecipeCategoryExtension<RECIPE>) ?: run {
                for ((clazz: Class<out HCForgingRecipe>, extension: HTForgingRecipeCategoryExtension<*>) in extensions) {
                    if (clazz.isInstance(recipe)) {
                        return@run extension as? HTForgingRecipeCategoryExtension<RECIPE>
                    }
                }
                null
            } ?: return null
        return (recipe as RECIPE) to extension
    }
}
