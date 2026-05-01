package hiiragi283.core.impl.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.listOrElement
import hiiragi283.core.common.data.recipe.builder.HTItemToMultiItemRecipeBuilder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

open class HTBasicItemToMultiItemRecipe(
    val ingredient: HTItemIngredient,
    val results: List<HTItemResult>,
    override val progressData: HTProgressData,
) : HTItemToMultiItemRecipe,
    HTProgressRecipe.Simple<SingleRecipeInput> {
    companion object {
        @JvmStatic
        fun <T : HTBasicItemToMultiItemRecipe> codec(
            outputRange: IntRange,
            factory: HTItemToMultiItemRecipeBuilder.Factory<T>,
        ): MapCodec<T> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTBasicItemToMultiItemRecipe::ingredient),
                    HTItemResult.CODEC
                        .listOrElement(outputRange)
                        .fieldOf(HTConst.RESULTS)
                        .forGetter(HTBasicItemToMultiItemRecipe::results),
                    HTProgressData.CODEC.forGetter(HTBasicItemToMultiItemRecipe::progressData),
                ).apply(instance, factory::create)
        }
    }

    override fun test(input: ItemStack): Boolean = ingredient.test(input)

    override fun getRequiredAmount(input: ItemStack): Int = ingredient.getRequiredAmount(input)

    override fun assemble(input: ItemStack): Iterable<ItemStack> = results.mapNotNull { it.get().value() }
}
