package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.HTRecipeFactory
import hiiragi283.core.api.recipe.base.predicate.HTDoubleRecipePredicate
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.BiFunction

/**
 * 空の容器に液体を汲み入れるレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.1
 */
interface HTTankFillingRecipe :
    HTDoubleRecipePredicate.ItemAndFluid,
    HTRecipeFactory<HTItemAndFluidRecipeInput, ItemStack>,
    BiFunction<ItemStack, FluidStack, ItemStack> {
    fun testContainer(stack: ItemStack): Boolean

    fun testFluid(stack: FluidStack): Boolean

    override fun apply(first: ItemStack, second: FluidStack): ItemStack

    override fun test(first: ItemStack, second: FluidStack): Boolean = testContainer(first) && testFluid(second)

    override fun assemble(input: HTItemAndFluidRecipeInput): ItemStack {
        val (item: ItemStack, fluid: FluidStack) = input
        return apply(item, fluid)
    }
}
