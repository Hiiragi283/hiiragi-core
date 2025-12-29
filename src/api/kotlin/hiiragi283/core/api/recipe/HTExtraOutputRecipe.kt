package hiiragi283.core.api.recipe

import hiiragi283.core.api.recipe.input.HTRecipeInput
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack

/**
 * 副産物をもつ[HTRecipe]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
interface HTExtraOutputRecipe : HTRecipe {
    fun assembleExtra(input: HTRecipeInput, provider: HolderLookup.Provider): ItemStack
}
