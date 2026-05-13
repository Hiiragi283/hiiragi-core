package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.base.HTRecipePredicates
import hiiragi283.lib.recipe.ingredient.getRequiredAmount
import hiiragi283.lib.recipe.result.HTChancedItemResult
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

open class HTInWorldRecipe(val ingredient: Ingredient, val result: HTChancedItemResult) :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<ItemStack> {
    companion object {
        @JvmStatic
        fun <T : HTInWorldRecipe> codec(factory: (Ingredient, HTChancedItemResult) -> T): MapCodec<T> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    Ingredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(HTInWorldRecipe::ingredient),
                    HTChancedItemResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(HTInWorldRecipe::result),
                ).apply(instance, factory)
        }

        @JvmField
        val SIMPLE_CODEC: MapCodec<HTInWorldRecipe> = codec(::HTInWorldRecipe)
    }

    override fun test(input: ItemStack): Boolean = ingredient.test(input)

    override fun getRequiredAmount(input: ItemStack): Int = ingredient.getRequiredAmount(input)

    override fun assemble(input: ItemStack): ItemStack = result.createOrEmpty()
}
