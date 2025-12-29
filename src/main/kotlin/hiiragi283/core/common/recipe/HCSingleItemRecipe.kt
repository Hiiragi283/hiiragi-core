package hiiragi283.core.common.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.common.data.recipe.builder.HTSingleItemRecipeBuilder
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

abstract class HCSingleItemRecipe(val ingredient: HTItemIngredient, val result: HTItemResult) : HTRecipe {
    companion object {
        @JvmStatic
        fun <RECIPE : HCSingleItemRecipe> codec(
            factory: HTSingleItemRecipeBuilder.Factory<RECIPE>,
        ): MapBiCodec<RegistryFriendlyByteBuf, RECIPE> = MapBiCodec.composite(
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HCSingleItemRecipe::ingredient),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCSingleItemRecipe::result),
            factory::create,
        )
    }

    final override fun matches(input: HTRecipeInput, level: Level): Boolean = input.testItem(0, ingredient)

    final override fun assemble(input: HTRecipeInput, registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)
}
