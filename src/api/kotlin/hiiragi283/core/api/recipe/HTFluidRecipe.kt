package hiiragi283.core.api.recipe

import hiiragi283.core.api.recipe.result.HTFluidResult
import net.minecraft.core.HolderLookup
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 完成品に[液体][FluidStack]を扱うレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
interface HTFluidRecipe {
    /**
     * 完成品の[液体][FluidStack]を取得します。
     */
    fun getResultFluid(registries: HolderLookup.Provider): FluidStack

    /**
     * [液体の完成品][HTFluidResult]に基づいた[HTFluidResult]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
    interface Simple : HTFluidRecipe {
        /**
         * [液体の完成品][HTFluidResult]を取得します。
         */
        val result: HTFluidResult

        override fun getResultFluid(registries: HolderLookup.Provider): FluidStack = result.getStackOrEmpty(registries)
    }
}
