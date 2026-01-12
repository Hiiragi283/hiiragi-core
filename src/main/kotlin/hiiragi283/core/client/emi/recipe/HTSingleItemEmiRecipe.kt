package hiiragi283.core.client.emi.recipe

import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.SupplierDataSource
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import hiiragi283.core.api.gui.element.addRowChild
import hiiragi283.core.api.integration.emi.HTEmiIngredientSlot
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.integration.emi.HTHolderModularEmiRecipe
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.client.emi.HCEmiRecipeCategories
import hiiragi283.core.common.recipe.HCAnvilCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCSingleItemRecipe
import hiiragi283.core.util.HTModularUIHelper
import net.minecraft.world.item.crafting.RecipeHolder

class HTSingleItemEmiRecipe<RECIPE : HCSingleItemRecipe>(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) :
    HTHolderModularEmiRecipe<RECIPE>(::createUI, category, holder) {
    companion object {
        @JvmStatic
        fun crushing(holder: RecipeHolder<HCAnvilCrushingRecipe>): HTSingleItemEmiRecipe<HCAnvilCrushingRecipe> =
            HTSingleItemEmiRecipe(HCEmiRecipeCategories.ANVIL_CRUSHING, holder)

        @JvmStatic
        fun exploding(holder: RecipeHolder<HCExplodingRecipe>): HTSingleItemEmiRecipe<HCExplodingRecipe> =
            HTSingleItemEmiRecipe(HCEmiRecipeCategories.EXPLODING, holder)

        @JvmStatic
        fun createUI(recipe: HCSingleItemRecipe): ModularUI = HTModularUIHelper.createVanillaUI(
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
