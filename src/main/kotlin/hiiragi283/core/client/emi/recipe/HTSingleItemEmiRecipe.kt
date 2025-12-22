package hiiragi283.core.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.HTEmiHolderRecipe
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.integration.emi.addArrow
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.client.emi.HCEmiRecipeCategories
import hiiragi283.core.common.recipe.HTCrushingRecipe
import hiiragi283.core.common.recipe.HTDryingRecipe
import hiiragi283.core.common.recipe.HTExplodingRecipe
import hiiragi283.core.common.recipe.HTSingleItemRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTSingleItemEmiRecipe<RECIPE : HTSingleItemRecipe>(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) :
    HTEmiHolderRecipe<RECIPE>(category, holder) {
    companion object {
        @JvmStatic
        fun crushing(holder: RecipeHolder<HTCrushingRecipe>): HTSingleItemEmiRecipe<HTCrushingRecipe> =
            HTSingleItemEmiRecipe(HCEmiRecipeCategories.CRUSHING, holder)

        @JvmStatic
        fun drying(holder: RecipeHolder<HTDryingRecipe>): HTSingleItemEmiRecipe<HTDryingRecipe> =
            HTSingleItemEmiRecipe(HCEmiRecipeCategories.DRYING, holder)

        @JvmStatic
        fun exploding(holder: RecipeHolder<HTExplodingRecipe>): HTSingleItemEmiRecipe<HTExplodingRecipe> =
            HTSingleItemEmiRecipe(HCEmiRecipeCategories.EXPLODING, holder)
    }

    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets
            .addArrow(getPosition(2.5), getPosition(1))
            .tooltipText(listOf(HTCommonTranslation.SECONDS.translate(recipe.time / 20f, recipe.time)))
        widgets.addText(
            HTCommonTranslation.STORED_EXP.translate(recipe.exp),
            getPosition(2.5),
            getPosition(2.5),
            -1,
            true,
        )

        // input
        widgets.addSlot(input(0), getPosition(1), getPosition(0.5))
        // outputs
        widgets.addOutput(0, getPosition(4.5), getPosition(1), true)
    }
}
