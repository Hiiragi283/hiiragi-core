package hiiragi283.core.common.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.MapBiCodec
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

abstract class HCSingleItemRecipe<INPUT : RecipeInput>(val ingredient: HTItemIngredient, val result: HTItemResult) : HTRecipe<INPUT> {
    companion object {
        @JvmStatic
        fun <RECIPE : HCSingleItemRecipe<*>> codec(
            factory: (HTItemIngredient, HTItemResult) -> RECIPE,
        ): MapBiCodec<RegistryFriendlyByteBuf, RECIPE> = MapBiCodec.composite(
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HCSingleItemRecipe<*>::ingredient),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCSingleItemRecipe<*>::result),
            factory,
        )
    }

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)
}
