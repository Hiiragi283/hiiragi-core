package hiiragi283.core.client.emi.recipe

import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.SupplierDataSource
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import hiiragi283.core.api.gui.element.addRowChild
import hiiragi283.core.api.integration.emi.HTEmiIngredientSlot
import hiiragi283.core.api.integration.emi.HTHolderModularEmiRecipe
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.client.emi.HCEmiRecipeCategories
import hiiragi283.core.common.recipe.HCLightningChargingRecipe
import hiiragi283.core.util.HTModularUIHelper
import net.minecraft.world.item.crafting.RecipeHolder

class HTChargingEmiRecipe(holder: RecipeHolder<HCLightningChargingRecipe>) :
    HTHolderModularEmiRecipe<HCLightningChargingRecipe>(::createUI, HCEmiRecipeCategories.CHARGING, holder) {
    companion object {
        @JvmStatic
        fun createUI(recipe: HCLightningChargingRecipe): ModularUI = HTModularUIHelper.createVanillaUI(
            UIElement()
                .layout { it.widthPercent(100f).heightPercent(100f) }
                .addRowChild {
                    addChild(
                        HTEmiIngredientSlot
                            .input()
                            .bindDataSource(SupplierDataSource.of(recipe.ingredient::toEmi)),
                    )
                    addChild(HTModularUIHelper.rightArrowIcon().layout { it.marginHorizontalPercent(10f) })
                    addChild(
                        HTEmiIngredientSlot
                            .output()
                            .bindDataSource(SupplierDataSource.of(recipe.result::toEmi)),
                    )
                },
        )
    }
}
