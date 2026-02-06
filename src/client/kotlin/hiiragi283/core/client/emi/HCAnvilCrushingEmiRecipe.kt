package hiiragi283.core.client.emi

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.emi.HTEmiHolderRecipe
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.common.recipe.HCAnvilCrushingRecipe
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeHolder

class HCAnvilCrushingEmiRecipe(holder: RecipeHolder<HCAnvilCrushingRecipe>) :
    HTEmiHolderRecipe<HCAnvilCrushingRecipe>(HCEmiRecipeCategories.ANVIL_CRUSHING, holder) {
    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
        addOutputs(recipe.extraResult?.toEmi())
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addSlot(
            Items.ANVIL.toEmi(),
            getPosition(1.5),
            getPosition(0),
        )

        widgets.addSlot(input(0), getPosition(0), getPosition(1), HTBackgroundType.INPUT)
        widgets.addArrow(getPosition(1.5), getPosition(1))
        widgets.addSlot(output(0), getPosition(3.5), getPosition(1), HTBackgroundType.OUTPUT)
        widgets.addSlot(output(1), getPosition(3.5), getPosition(0), HTBackgroundType.EXTRA_OUTPUT)
    }
}
