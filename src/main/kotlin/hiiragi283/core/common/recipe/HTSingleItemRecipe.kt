package hiiragi283.core.common.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.stack.ImmutableItemStack
import hiiragi283.core.common.data.recipe.builder.HTSingleItemRecipeBuilder
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.level.Level
import org.apache.commons.lang3.math.Fraction

abstract class HTSingleItemRecipe(
    private val group: String,
    val ingredient: HTItemIngredient,
    val result: HTItemResult,
    time: Int,
    exp: Fraction,
) : HTProcessingRecipe(time, exp) {
    companion object {
        @JvmStatic
        fun <RECIPE : HTSingleItemRecipe> codec(
            factory: HTSingleItemRecipeBuilder.Factory<RECIPE>,
        ): MapBiCodec<RegistryFriendlyByteBuf, RECIPE> = MapBiCodec.composite(
            BiCodec.STRING.optionalFieldOf(HTConst.GROUP, "").forGetter(HTSingleItemRecipe::getGroup),
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTSingleItemRecipe::ingredient),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTSingleItemRecipe::result),
            TIME_CODEC.forGetter(HTSingleItemRecipe::time),
            EXP_CODEC.forGetter(HTSingleItemRecipe::exp),
            factory::create,
        )
    }

    final override fun matches(input: HTRecipeInput, level: Level): Boolean = input.testItem(0, ingredient)

    final override fun getGroup(): String = group

    final override fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        result.getStackOrNull(provider)
}
