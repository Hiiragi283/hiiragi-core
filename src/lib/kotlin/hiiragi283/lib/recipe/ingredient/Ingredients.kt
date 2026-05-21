package hiiragi283.lib.recipe.ingredient

import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

/**
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
fun Ingredient.getRequiredAmount(instance: TypedInstance<Item>): Int = when {
    HTIngredientHelper.createStack(instance).let(this::test) -> 1
    else -> 0
}

/**
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
fun FluidIngredient.getRequiredAmount(instance: TypedInstance<Fluid>): Int = when {
    HTIngredientHelper.createStack(instance).let(this::test) -> FluidType.BUCKET_VOLUME
    else -> 0
}
