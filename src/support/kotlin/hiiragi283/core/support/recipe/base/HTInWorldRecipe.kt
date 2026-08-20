package hiiragi283.core.support.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTRecipeFactories
import hiiragi283.core.api.recipe.base.HTRecipePredicates
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import net.minecraft.world.item.ItemStack

open class HTInWorldRecipe(val ingredient: HTItemIngredient, val result: HTChancedItemResult) :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<ItemStack> {
    companion object {
        @JvmStatic
        fun <T : HTInWorldRecipe> codec(factory: (HTItemIngredient, HTChancedItemResult) -> T): MapCodec<T> = HTCodecs.recordMap { instance ->
            instance
                .group(
                    HTItemIngredient.SINGLE_CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTInWorldRecipe::ingredient),
                    HTChancedItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTInWorldRecipe::result),
                ).apply(instance, factory)
        }

        @JvmField
        val SIMPLE_CODEC: MapCodec<HTInWorldRecipe> = codec(::HTInWorldRecipe)
    }

    override fun test(input: ItemStack): Boolean = ingredient.test(input)

    override fun getMatchingStack(input: ItemStack): ItemStack = ingredient.getMatchingStack(input)

    override fun apply(input: ItemStack): ItemStack = result.createOrEmpty()

    override fun isIncomplete(): Boolean = ingredient.isIncomplete() || result.isIncomplete()
}
