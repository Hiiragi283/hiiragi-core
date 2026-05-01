package hiiragi283.core.impl.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTRecipeFactory
import hiiragi283.core.api.recipe.base.predicate.HTSingleRecipePredicate
import hiiragi283.core.api.recipe.ingredient.getRequiredAmount
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SingleRecipeInput

open class HTInWorldRecipe(val ingredient: Ingredient, val result: HTItemResult) :
    HTSingleRecipePredicate.SingleItem,
    HTRecipeFactory<SingleRecipeInput, ItemStack> {
    companion object {
        @JvmStatic
        fun <T : HTInWorldRecipe> codec(factory: (Ingredient, HTItemResult) -> T): MapCodec<T> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTCodecs.INGREDIENT.fieldOf(HTConst.INGREDIENT).forGetter(HTInWorldRecipe::ingredient),
                    HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTInWorldRecipe::result),
                ).apply(instance, factory)
        }

        @JvmField
        val SIMPLE_CODEC: MapCodec<HTInWorldRecipe> = codec(::HTInWorldRecipe)
    }

    override fun test(input: ItemStack): Boolean = ingredient.test(input)

    override fun getRequiredAmount(input: ItemStack): Int = ingredient.getRequiredAmount(input)

    override fun assemble(input: SingleRecipeInput): ItemStack = result.getOrEmpty()
}
