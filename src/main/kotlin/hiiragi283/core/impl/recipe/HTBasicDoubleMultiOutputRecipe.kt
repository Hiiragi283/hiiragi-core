package hiiragi283.core.impl.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.listOrElement
import hiiragi283.core.common.data.recipe.builder.HTDoubleMultiOutputRecipeBuilder
import net.minecraft.world.item.ItemStack
import java.util.Optional

abstract class HTBasicDoubleMultiOutputRecipe(
    val base: HTItemIngredient,
    val addition: Optional<HTItemIngredient>,
    results: List<HTItemResult>,
    time: Int,
) : HTBasicMultiOutputRecipe<HTDoubleRecipeInput>(results, time),
    HTDoubleMultiOutputRecipe.Serializable {
    companion object {
        @JvmStatic
        fun <T : HTBasicDoubleMultiOutputRecipe> codec(
            outputRange: IntRange,
            factory: HTDoubleMultiOutputRecipeBuilder.Factory<T>,
        ): MapCodec<T> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf("base").forGetter(HTBasicDoubleMultiOutputRecipe::base),
                    HTItemIngredient.CODEC.optionalFieldOf("addition").forGetter(HTBasicDoubleMultiOutputRecipe::addition),
                    HTItemResult.CODEC
                        .listOrElement(outputRange)
                        .fieldOf(HTConst.RESULTS)
                        .forGetter(HTBasicDoubleMultiOutputRecipe::results),
                    HTProcessingRecipe.timeCodec(),
                ).apply(instance, factory::create)
        }
    }

    final override fun test(input: HTDoubleRecipeInput): Boolean {
        val (first: ItemStack, second: ItemStack) = input
        return base.test(first) && addition.map { it.test(second) }.orElseGet { true }
    }

    final override fun getBaseAmount(input: HTDoubleRecipeInput): Int = base.getRequiredAmount(input.first)

    final override fun getAdditionAmount(input: HTDoubleRecipeInput): Int =
        addition.map { it.getRequiredAmount(input.second) }.orElseGet { 0 }
}
