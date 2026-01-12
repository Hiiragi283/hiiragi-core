package hiiragi283.core.client.emi

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import hiiragi283.core.api.gui.HTModularUIHelper
import hiiragi283.core.api.gui.element.addRowChild
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.integration.emi.HTHolderModularEmiRecipe
import hiiragi283.core.common.recipe.HCAnvilCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCSingleItemRecipe
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
        fun createUI(recipe: HCSingleItemRecipe, root: UIElement) {
            root
                .addRowChild {
                    addChild(inputSlot(recipe.ingredient))
                    addChild(HTModularUIHelper.rightArrowIcon().layout { it.marginHorizontalPercent(10f) })
                    addChild(outputSlot(recipe.result))
                }
        }
    }
}
