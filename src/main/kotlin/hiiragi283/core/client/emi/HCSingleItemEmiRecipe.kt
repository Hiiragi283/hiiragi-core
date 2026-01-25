package hiiragi283.core.client.emi

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.emi.HTEmiHolderRecipe
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.common.recipe.HCAnvilCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCLightningChargingRecipe
import hiiragi283.core.common.recipe.HCSingleItemRecipe
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeHolder

class HCSingleItemEmiRecipe<RECIPE : HCSingleItemRecipe<*>>(
    category: HTEmiRecipeCategory,
    holder: RecipeHolder<RECIPE>,
    private val iconFactory: (RECIPE) -> ItemStack,
) : HTEmiHolderRecipe<RECIPE>(category, holder) {
    companion object {
        @JvmStatic
        fun charging(holder: RecipeHolder<HCLightningChargingRecipe>): HCSingleItemEmiRecipe<HCLightningChargingRecipe> =
            HCSingleItemEmiRecipe(
                HCEmiRecipeCategories.CHARGING,
                holder,
            ) { ItemStack(Items.LIGHTNING_ROD) }

        @JvmStatic
        fun crushing(holder: RecipeHolder<HCAnvilCrushingRecipe>): HCSingleItemEmiRecipe<HCAnvilCrushingRecipe> = HCSingleItemEmiRecipe(
            HCEmiRecipeCategories.ANVIL_CRUSHING,
            holder,
        ) { ItemStack(Items.ANVIL) }

        @JvmStatic
        fun exploding(holder: RecipeHolder<HCExplodingRecipe>): HCSingleItemEmiRecipe<HCExplodingRecipe> = HCSingleItemEmiRecipe(
            HCEmiRecipeCategories.EXPLODING,
            holder,
        ) { recipe: HCExplodingRecipe -> HCExplodingRecipe.createIcon(recipe.minPower.toFloat()) }
    }

    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addSlot(
            iconFactory(recipe).toEmi(),
            getPosition(1.5),
            getPosition(0),
        )

        widgets.addSlot(input(0), getPosition(0), getPosition(1), HTBackgroundType.INPUT)
        widgets.addArrow(getPosition(1.5), getPosition(1))
        widgets.addSlot(output(0), getPosition(3.5), getPosition(1), HTBackgroundType.OUTPUT)
    }
}
