package hiiragi283.core.impl.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.base.HTRecipeFactories
import hiiragi283.core.api.recipe.base.HTRecipePredicates
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

open class HTItemToItemRecipe(val ingredient: HTItemIngredient, val result: HTItemResult, override val progressData: HTProgressData) :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<ItemStack>,
    HTProgressRecipe.Simple<SingleRecipeInput> {
    companion object {
        @JvmStatic
        fun <T : HTItemToItemRecipe> codec(factory: (HTItemIngredient, HTItemResult, HTProgressData) -> T): MapCodec<T> =
            RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTItemToItemRecipe::ingredient),
                        HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTItemToItemRecipe::result),
                        HTProgressData.CODEC.forGetter(HTItemToItemRecipe::progressData),
                    ).apply(instance, factory)
            }

        @JvmField
        val SIMPLE_CODEC: MapCodec<HTItemToItemRecipe> = codec(::HTItemToItemRecipe)
    }

    override fun test(input: ItemStack): Boolean = ingredient.test(input)

    override fun getRequiredAmount(input: ItemStack): Int = ingredient.getRequiredAmount(input)

    override fun assemble(input: ItemStack): ItemStack = result.getOrEmpty()
}
