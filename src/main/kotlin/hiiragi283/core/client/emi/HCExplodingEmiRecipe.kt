package hiiragi283.core.client.emi

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot
import hiiragi283.core.api.gui.HTModularUIHelper
import hiiragi283.core.api.gui.element.addRowChild
import hiiragi283.core.api.integration.emi.HTHolderModularEmiRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HCExplodingEmiRecipe(holder: RecipeHolder<HCExplodingRecipe>) :
    HTHolderModularEmiRecipe<HCExplodingRecipe>({ recipe: HCExplodingRecipe, root: UIElement ->
        root
            .addRowChild {
                addChild(inputSlot(recipe.ingredient))
                addChild(HTModularUIHelper.rightArrowIcon().layout { it.marginHorizontalPercent(5f) })
                addChild(ItemSlot().setItem(HCExplodingRecipe.createIcon(recipe.minPower.toFloat())))
                addChild(HTModularUIHelper.rightArrowIcon().layout { it.marginHorizontalPercent(5f) })
                addChild(outputSlot(recipe.result))
            }
    }, HCEmiRecipeCategories.EXPLODING, holder)
