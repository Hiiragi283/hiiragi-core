package hiiragi283.core.impl.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTItemToItemRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.common.data.recipe.builder.HTItemToResultRecipeBuilder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

open class HTBasicItemToItemRecipe(val ingredient: HTItemIngredient, val result: HTItemResult, override val progressData: HTProgressData) :
    HTItemToItemRecipe,
    HTProgressRecipe.Simple<SingleRecipeInput> {
    companion object {
        @JvmStatic
        fun <T : HTBasicItemToItemRecipe> codec(factory: HTItemToResultRecipeBuilder.Factory<HTItemResult, T>): MapCodec<T> = HTCodecs.recordMap { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTBasicItemToItemRecipe::ingredient),
                    HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTBasicItemToItemRecipe::result),
                    HTProgressData.CODEC.forGetter(HTBasicItemToItemRecipe::progressData),
                ).apply(instance, factory::create)
        }

        @JvmField
        val SIMPLE_CODEC: MapCodec<HTBasicItemToItemRecipe> = codec(::HTBasicItemToItemRecipe)
    }

    override fun test(input: ItemStack): Boolean = ingredient.test(input)

    override fun getRequiredAmount(input: ItemStack): Int = ingredient.getRequiredAmount(input)

    override fun assemble(input: ItemStack): ItemStack = result.createOrEmpty()
}
