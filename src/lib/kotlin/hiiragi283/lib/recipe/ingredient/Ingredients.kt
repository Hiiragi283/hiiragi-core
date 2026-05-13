package hiiragi283.lib.recipe.ingredient

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
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
    this.test(input) -> FluidType.BUCKET_VOLUME
    else -> 0
}
