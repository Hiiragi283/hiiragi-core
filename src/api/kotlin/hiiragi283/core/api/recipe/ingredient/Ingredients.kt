package hiiragi283.core.api.recipe.ingredient

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

fun Ingredient.getMatchingStack(input: ItemStack): ItemStack = when {
    this.test(input) -> input.copyWithCount(1)
    else -> ItemStack.EMPTY
}
