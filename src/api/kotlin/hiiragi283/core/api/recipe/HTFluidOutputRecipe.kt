package hiiragi283.core.api.recipe

import hiiragi283.core.api.recipe.input.HTRecipeInput
import net.minecraft.core.HolderLookup
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 液体の完成品をもつ[HTRecipe]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
interface HTFluidOutputRecipe : HTRecipe {
    /**
     * 指定した[入力][input]と[レジストリ][provider]から完成品を返します。
     * @return 完成品がない場合は[FluidStack.EMPTY]
     */
    fun assembleFluid(input: HTRecipeInput, provider: HolderLookup.Provider): FluidStack
}
