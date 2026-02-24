package hiiragi283.core.client.jei.extension

import hiiragi283.core.common.crafting.HCEternalSmithingRecipe
import hiiragi283.core.setup.HCItems
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension
import mezz.jei.api.runtime.IIngredientManager
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.Unbreakable

class HCEternalSmithingCategoryExtension(private val manager: IIngredientManager) :
    ISmithingCategoryExtension<HCEternalSmithingRecipe> {
    override fun <T : IIngredientAcceptor<T>> setTemplate(recipe: HCEternalSmithingRecipe, ingredientAcceptor: T) {
        ingredientAcceptor.addItemLike(HCItems.ETERNAL_UPGRADE)
    }

    override fun <T : IIngredientAcceptor<T>> setBase(recipe: HCEternalSmithingRecipe, ingredientAcceptor: T) {
        ingredientAcceptor.addItemStacks(manager.allItemStacks.filter { it.isDamageableItem })
    }

    override fun <T : IIngredientAcceptor<T>> setAddition(recipe: HCEternalSmithingRecipe, ingredientAcceptor: T) {
        ingredientAcceptor.addIngredients(HCEternalSmithingRecipe.ADDITIONAL_TAG)
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HCEternalSmithingRecipe,
        templateSlot: IRecipeSlotDrawable,
        baseSlot: IRecipeSlotDrawable,
        additionSlot: IRecipeSlotDrawable,
        outputSlot: IRecipeSlotDrawable,
        focuses: IFocusGroup,
    ) {
        val baseStack: ItemStack = baseSlot.displayedItemStack.orElse(ItemStack.EMPTY).copy()
        if (baseStack.isEmpty) return
        baseStack.set(DataComponents.UNBREAKABLE, Unbreakable(true))

        outputSlot
            .createDisplayOverrides()
            .addItemStack(baseStack)
    }
}
