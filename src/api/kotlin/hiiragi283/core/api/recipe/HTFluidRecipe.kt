package hiiragi283.core.api.recipe

import hiiragi283.core.api.recipe.result.HTFluidResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 完成品に[液体][FluidStack]を扱うレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
fun interface HTFluidRecipe<INPUT : RecipeInput> {
    /**
     * 完成品の[液体][FluidStack]を取得します。
     */
    fun assembleFluid(input: INPUT, registries: HolderLookup.Provider): FluidStack

    /**
     * [液体の完成品][HTFluidResult]に基づいた[HTFluidResult]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
    fun interface Simple<INPUT : RecipeInput> : HTFluidRecipe<INPUT> {
        override fun assembleFluid(input: INPUT, registries: HolderLookup.Provider): FluidStack = getResultFluid(registries)

        /**
         * 完成品の[液体][FluidStack]を取得します。
         */
        fun getResultFluid(registries: HolderLookup.Provider): FluidStack
    }
}
