package hiiragi283.core.impl.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTRecipeFactories
import hiiragi283.core.api.recipe.base.HTRecipePredicates
import hiiragi283.core.api.recipe.ingredient.getRequiredAmount
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

open class HTInWorldRecipe(val ingredient: Ingredient, val result: HTChancedItemResult) :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<ItemStack> {
    companion object {
        @JvmStatic
        fun <T : HTInWorldRecipe> codec(factory: (Ingredient, HTChancedItemResult) -> T): MapCodec<T> = HTCodecs.recordMap { instance ->
            instance
                .group(
                    HTCodecs.INGREDIENT.fieldOf(HTConst.INGREDIENT).forGetter(HTInWorldRecipe::ingredient),
                    HTChancedItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTInWorldRecipe::result),
                ).apply(instance, factory)
        }

        @JvmField
        val SIMPLE_CODEC: MapCodec<HTInWorldRecipe> = codec(::HTInWorldRecipe)
    }

    override fun test(input: ItemStack): Boolean = ingredient.test(input)

    override fun getRequiredAmount(input: ItemStack): Int = ingredient.getRequiredAmount(input)

    override fun assemble(input: ItemStack): ItemStack = result.createOrEmpty()
}
