package hiiragi283.core.api.recipe.base

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 空の容器に液体を汲み入れるレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.1
 */
interface HTTankFillingRecipe :
    HTRecipePredicates.ItemAndFluid,
    HTRecipeFactories.ItemAndFluid<ItemStack> {
    fun testContainer(stack: ItemStack): Boolean

    fun testFluid(stack: FluidStack): Boolean

    override fun test(first: ItemStack, second: FluidStack): Boolean = testContainer(first) && testFluid(second)
}
