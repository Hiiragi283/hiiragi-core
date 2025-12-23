package hiiragi283.core.api.recipe

import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.stack.ImmutableFluidStack
import net.minecraft.core.HolderLookup

/**
 * 完成品に液体を扱える[HTAbstractRecipe]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
interface HTFluidOutputRecipe : HTAbstractRecipe {
    /**
     * 指定した[入力][input]と[レジストリ][provider]から完成品を返します。
     * @return 完成品がない場合は`null`
     */
    fun assembleFluid(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableFluidStack?
}
