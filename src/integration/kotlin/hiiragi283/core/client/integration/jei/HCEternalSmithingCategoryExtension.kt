package hiiragi283.core.client.integration.jei

import hiiragi283.core.common.recipe.custom.HCEternalSmithingRecipe
import hiiragi283.core.setup.HCItems
import hiiragi283.lib.util.kotlin
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension
import mezz.jei.api.runtime.IIngredientManager
import net.minecraft.core.component.DataComponents
import net.minecraft.util.Unit
import net.minecraft.world.item.ItemStack

class HCEternalSmithingCategoryExtension(private val manager: IIngredientManager) : ISmithingCategoryExtension<HCEternalSmithingRecipe> {
    private val toolStacks: List<ItemStack> by lazy { manager.allItemStacks.filter { it.isDamageableItem } }

    override fun <T : IIngredientAcceptor<T>> setTemplate(recipe: HCEternalSmithingRecipe, ingredientAcceptor: T) {
        ingredientAcceptor.add(HCItems.ETERNAL_UPGRADE)
    }

    override fun <T : IIngredientAcceptor<T>> setBase(recipe: HCEternalSmithingRecipe, ingredientAcceptor: T) {
        ingredientAcceptor.addItemStacks(toolStacks)
    }

    override fun <T : IIngredientAcceptor<T>> setAddition(recipe: HCEternalSmithingRecipe, ingredientAcceptor: T) {
        HCEternalSmithingRecipe.additionIngredient().ifPresent(ingredientAcceptor::add)
    }

    override fun <T : IIngredientAcceptor<T>> setOutput(recipe: HCEternalSmithingRecipe, ingredientAcceptor: T) {
        ingredientAcceptor.addItemStacks(toolStacks)
    }

    override fun onDisplayedIngredientsUpdate(recipe: HCEternalSmithingRecipe, templateSlot: IRecipeSlotDrawable, baseSlot: IRecipeSlotDrawable, additionSlot: IRecipeSlotDrawable, outputSlot: IRecipeSlotDrawable, focuses: IFocusGroup) {
        baseSlot.displayedItemStack
            .kotlin
            .filterNot(ItemStack::isEmpty)
            .map(ItemStack::copy)
            .onSome { baseStack: ItemStack ->
                baseStack.set(DataComponents.UNBREAKABLE, Unit.INSTANCE)
                outputSlot.createDisplayOverrides().add(baseStack)
            }
    }
}
