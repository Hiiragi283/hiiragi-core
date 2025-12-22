package hiiragi283.core.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.HTEmiHolderRecipe
import hiiragi283.core.api.integration.emi.addArrow
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.client.emi.HCEmiRecipeCategories
import hiiragi283.core.common.recipe.HTChargingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTChargingEmiRecipe(holder: RecipeHolder<HTChargingRecipe>) :
    HTEmiHolderRecipe<HTChargingRecipe>(HCEmiRecipeCategories.CHARGING, holder) {
    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets
            .addArrow(getPosition(2.5), getPosition(1))
            .tooltipText(listOf(HTCommonTranslation.STORED_FE.translate(recipe.energy)))
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
