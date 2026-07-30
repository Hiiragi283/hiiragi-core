package hiiragi283.core.api.recipe.ingredient

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun Ingredient.getMatchingStack(input: ItemStack): ItemStack = when {
    this.test(input) -> input.copyWithCount(1)
    else -> ItemStack.EMPTY
}
