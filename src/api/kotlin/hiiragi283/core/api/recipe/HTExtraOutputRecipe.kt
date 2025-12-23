package hiiragi283.core.api.recipe

import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup

/**
 * 副産物をもつ[HTAbstractRecipe]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
interface HTExtraOutputRecipe : HTAbstractRecipe {
    fun assembleExtra(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack?
}
