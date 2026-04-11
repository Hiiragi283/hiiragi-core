package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.util.Ior
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Predicate

typealias ItemAmount = Int
typealias FluidAmount = Int

/**
 * 一つのアイテムまたは液体を，一つのアイテムまたは液体に変換するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
interface HTItemOrFluidRecipe :
    HTProcessingRecipe<HTItemAndFluidRecipeInput>,
    HTFluidRecipe<HTItemAndFluidRecipeInput> {
    override fun test(input: HTItemAndFluidRecipeInput): Boolean {
        val (item: ItemStack, fluid: FluidStack) = input
        return getPredicate().fold(
            { it.test(item) && fluid.isEmpty },
            { it.test(fluid) && item.isEmpty },
            { itemPre: Predicate<ItemStack>, fluidPre: Predicate<FluidStack> -> itemPre.test(item) && fluidPre.test(fluid) },
        )
    }

    fun getPredicate(): Ior<Predicate<ItemStack>, Predicate<FluidStack>>

    fun getRequiredAmount(input: HTItemAndFluidRecipeInput): Ior<ItemAmount, FluidAmount>

    //    Serializable    //

    /**
     * シリアライズ可能な[HTItemOrFluidRecipe]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.15.0
     */
    interface Serializable :
        HTItemOrFluidRecipe,
        HTProcessingRecipe.Serializable<HTItemAndFluidRecipeInput>
}
