package hiiragi283.core.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.HTEmiHolderRecipe
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.integration.emi.addArrow
import hiiragi283.core.client.emi.HCEmiRecipeCategories
import hiiragi283.core.common.recipe.HCAnvilCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCSingleItemRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTSingleItemEmiRecipe<RECIPE : HCSingleItemRecipe>(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) :
    HTEmiHolderRecipe<RECIPE>(category, holder) {
    companion object {
        @JvmStatic
        fun crushing(holder: RecipeHolder<HCAnvilCrushingRecipe>): HTSingleItemEmiRecipe<HCAnvilCrushingRecipe> =
            HTSingleItemEmiRecipe(HCEmiRecipeCategories.CRUSHING, holder)

        @JvmStatic
        fun exploding(holder: RecipeHolder<HCExplodingRecipe>): HTSingleItemEmiRecipe<HCExplodingRecipe> =
            HTSingleItemEmiRecipe(HCEmiRecipeCategories.EXPLODING, holder)
    }

    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        // input
        widgets.addSlot(input(0), getPosition(1), getPosition(0.5))
        // outputs
        widgets.addOutput(0, getPosition(4.5), getPosition(1), true)
    }
}
