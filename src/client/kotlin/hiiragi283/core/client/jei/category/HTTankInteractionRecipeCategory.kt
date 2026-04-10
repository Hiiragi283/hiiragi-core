package hiiragi283.core.client.jei.category

import hiiragi283.core.api.data.tank.HTTankInteraction
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.client.jei.HCJeiRecipeTypes
import hiiragi283.core.client.jei.category.base.HTLookupRecipeCategory
import hiiragi283.core.client.jei.extension.HTTankInteractionCE
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole

private typealias InteractionCE = HTTankInteractionCE<HTTankInteraction>

class HTTankInteractionRecipeCategory(guiHelper: IGuiHelper) :
    HTLookupRecipeCategory<HTTankInteraction>(guiHelper, HCJeiRecipeTypes.TANK_INTERACTION) {
    private val extensions: MutableMap<Class<out HTTankInteraction>, HTTankInteractionCE<*>> = hashMapOf()

    inline fun <reified RECIPE : HTTankInteraction> addExtension(extension: HTTankInteractionCE<RECIPE>) {
        this.addExtension(RECIPE::class.java, extension)
    }

    fun <RECIPE : HTTankInteraction> addExtension(clazz: Class<RECIPE>, extension: HTTankInteractionCE<RECIPE>) {
        extensions[clazz] = extension
    }

    //    HTLookupRecipeCategory    //

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTTankInteraction, focuses: IFocusGroup) {
        val (recipe1: HTTankInteraction, extension: InteractionCE) = getExtension<HTTankInteraction>(recipe) ?: return
        // filling
        val fillInput: IRecipeSlotBuilder =
            builder.addInputSlot().setPosition(getPosition(0), getPosition(0)).setSlotBackground(HTBackgroundType.INPUT)
        val fillOutput: IRecipeSlotBuilder =
            builder.addOutputSlot().setPosition(getPosition(0), getPosition(2)).setSlotBackground(HTBackgroundType.OUTPUT)
        if (extension.canFill) {
            extension.setEmptyContainer(recipe1, fillInput)
            extension.setFilledContainer(recipe1, fillOutput)
        }
        // fluid
        extension.setFluid(
            recipe1,
            builder
                .addSlot(RecipeIngredientRole.CATALYST)
                .setPosition(getPosition(2), getPosition(0))
                .setFluidRenderer(recipe1.amount.toLong(), false, 16, 18 * 3 - 2)
                .setTankBackground(HTBackgroundType.NONE),
        )
        // emptying
        val emptyInput: IRecipeSlotBuilder =
            builder.addInputSlot().setPosition(getPosition(4), getPosition(0)).setSlotBackground(HTBackgroundType.EXTRA_INPUT)
        val emptyOutput: IRecipeSlotBuilder =
            builder.addOutputSlot().setPosition(getPosition(4), getPosition(2)).setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
        if (extension.canEmpty) {
            extension.setFilledContainer(recipe1, emptyInput)
            extension.setEmptyContainer(recipe1, emptyOutput)
        }
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HTRecipeHolder<HTTankInteraction>,
        recipeSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {
        val (recipe1: HTTankInteraction, extension: InteractionCE) = getExtension<HTTankInteraction>(recipe.recipe) ?: return
        if (extension.canFill) {
            extension.onDisplayedIngredientsUpdate(
                recipe1,
                recipeSlots[0],
                recipeSlots[1],
                recipeSlots[2],
                focuses,
            )
        }
        if (extension.canEmpty) {
            extension.onDisplayedIngredientsUpdate(
                recipe1,
                recipeSlots[4],
                recipeSlots[3],
                recipeSlots[2],
                focuses,
            )
        }
    }

    override fun isHandled(recipe: HTRecipeHolder<HTTankInteraction>): Boolean = getExtension<HTTankInteraction>(recipe.recipe) != null

    @Suppress("UNCHECKED_CAST")
    private fun <RECIPE : HTTankInteraction> getExtension(recipe: HTTankInteraction): Pair<RECIPE, HTTankInteractionCE<RECIPE>>? {
        val extension: HTTankInteractionCE<RECIPE> =
            (extensions[recipe::class.java] as? HTTankInteractionCE<RECIPE>) ?: run {
                for ((clazz: Class<out HTTankInteraction>, extension: HTTankInteractionCE<*>) in extensions) {
                    if (clazz.isInstance(recipe)) {
                        return@run extension as? HTTankInteractionCE<RECIPE>
                    }
                }
                null
            } ?: return null
        return (recipe as RECIPE) to extension
    }
}
