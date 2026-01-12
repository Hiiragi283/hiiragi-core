package hiiragi283.core.client.emi

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import hiiragi283.core.api.gui.HTModularUIHelper
import hiiragi283.core.api.gui.element.addRowChild
import hiiragi283.core.api.integration.emi.HTHolderModularEmiRecipe
import hiiragi283.core.common.recipe.HCLightningChargingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTChargingEmiRecipe(holder: RecipeHolder<HCLightningChargingRecipe>) :
    HTHolderModularEmiRecipe<HCLightningChargingRecipe>(::createUI, HCEmiRecipeCategories.CHARGING, holder) {
    companion object {
        @JvmStatic
        fun createUI(recipe: HCLightningChargingRecipe, root: UIElement) {
            root
                .addRowChild {
                    addChild(inputSlot(recipe.ingredient))
                    addChild(HTModularUIHelper.rightArrowIcon().layout { it.marginHorizontalPercent(10f) })
                    addChild(outputSlot(recipe.result))
                }
        }
    }
}
