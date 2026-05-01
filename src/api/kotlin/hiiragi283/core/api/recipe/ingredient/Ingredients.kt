package hiiragi283.core.api.recipe.ingredient

import hiiragi283.core.api.HTConst
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

/**
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
fun Ingredient.getRequiredAmount(input: ItemStack): Int = when {
    this.test(input) -> 1
    else -> 0
}

/**
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
fun FluidIngredient.getRequiredAmount(input: FluidStack): Int = when {
    this.test(input) -> HTConst.DEFAULT_FLUID_AMOUNT
    else -> 0
}
