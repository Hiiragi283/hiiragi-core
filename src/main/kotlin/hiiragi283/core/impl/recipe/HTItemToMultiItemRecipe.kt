package hiiragi283.core.impl.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.base.factory.HTMultiRecipeFactory
import hiiragi283.core.api.recipe.base.predicate.HTSingleRecipePredicate
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.listOrElement
import hiiragi283.core.common.data.recipe.builder.HTItemToMultiItemRecipeBuilder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

open class HTItemToMultiItemRecipe(
    val ingredient: HTItemIngredient,
    val results: List<HTItemResult>,
    override val progressData: HTProgressData,
) : HTSingleRecipePredicate.SingleItem,
    HTMultiRecipeFactory.ItemTo<Iterable<ItemStack>>,
    HTProgressRecipe.Simple<SingleRecipeInput> {
    companion object {
        @JvmStatic
        fun <T : HTItemToMultiItemRecipe> codec(outputRange: IntRange, factory: HTItemToMultiItemRecipeBuilder.Factory<T>): MapCodec<T> =
            RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTItemToMultiItemRecipe::ingredient),
                        HTItemResult.CODEC
                            .listOrElement(outputRange)
                            .fieldOf(HTConst.RESULTS)
                            .forGetter(HTItemToMultiItemRecipe::results),
                        HTProgressData.CODEC.forGetter(HTItemToMultiItemRecipe::progressData),
                    ).apply(instance, factory::create)
            }
    }

    override fun test(input: ItemStack): Boolean = ingredient.test(input)

    override fun getRequiredAmount(input: ItemStack): Int = ingredient.getRequiredAmount(input)

    override fun apply(input: ItemStack): Iterable<ItemStack> = results.mapNotNull { it.get().value() }
}
