package hiiragi283.core.support.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTRecipeResultHelper
import hiiragi283.core.api.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.codec.listOrElement
import hiiragi283.core.support.data.recipe.HTItemToMultiItemRecipeBuilder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

open class HTBasicItemToMultiItemRecipe(
    val ingredient: HTItemIngredient,
    val results: List<HTChancedItemResult>,
    override val progressData: HTProgressData,
) : HTItemToMultiItemRecipe,
    HTProgressRecipe.Simple<SingleRecipeInput> {
    companion object {
        @JvmStatic
        fun <T : HTBasicItemToMultiItemRecipe> codec(maxSize: Int, factory: HTItemToMultiItemRecipeBuilder.Factory<T>): MapCodec<T> = HTCodecs.recordMap { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTBasicItemToMultiItemRecipe::ingredient),
                    HTChancedItemResult.CODEC
                        .listOrElement(1, maxSize)
                        .fieldOf(HTConst.RESULTS)
                        .forGetter(HTBasicItemToMultiItemRecipe::results),
                    HTProgressData.CODEC.forGetter(HTBasicItemToMultiItemRecipe::progressData),
                ).apply(instance, factory::create)
        }
    }

    override fun test(input: ItemStack): Boolean = ingredient.test(input)

    override fun getMatchingStack(input: ItemStack): ItemStack = ingredient.getMatchingStack(input)

    override fun apply(input: ItemStack): Iterable<ItemStack> = results.map(HTChancedItemResult::createOrEmpty).let(HTRecipeResultHelper::mergeStacks)

    override fun isIncomplete(): Boolean = ingredient.isIncomplete() || results.any { it.isIncomplete() }
}
