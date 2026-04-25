package hiiragi283.core.impl.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.listOrElement
import hiiragi283.core.common.data.recipe.builder.HTSingleMultiOutputRecipeBuilder
import net.minecraft.world.item.crafting.SingleRecipeInput

abstract class HTBasicSingleMultiOutputRecipe(val ingredient: HTItemIngredient, results: List<HTItemResult>, time: Int) :
    HTBasicMultiOutputRecipe<SingleRecipeInput>(results, time),
    HTSingleMultiOutputRecipe.Serializable {
    companion object {
        @JvmStatic
        fun <T : HTBasicSingleMultiOutputRecipe> codec(
            outputRange: IntRange,
            factory: HTSingleMultiOutputRecipeBuilder.Factory<T>,
        ): MapCodec<T> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTBasicSingleMultiOutputRecipe::ingredient),
                    HTItemResult.CODEC
                        .listOrElement(outputRange)
                        .fieldOf(HTConst.RESULTS)
                        .forGetter(HTBasicSingleMultiOutputRecipe::results),
                    HTProcessingRecipe.timeCodec(),
                ).apply(instance, factory::create)
        }
    }

    final override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    final override fun getRequiredAmount(input: SingleRecipeInput): Int = ingredient.getRequiredAmount(input.item())
}
