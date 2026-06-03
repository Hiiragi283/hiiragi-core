package hiiragi283.lib.recipe.base.impl

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.HTItemToChancedItemsRecipeBuilder
import hiiragi283.lib.recipe.base.HTItemToChancedItemsRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTChancedItemResult
import hiiragi283.lib.recipe.result.HTResultHelper
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.listOrElement
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

open class HTBasicItemToChancedItemsRecipe(
    val ingredient: HTItemIngredient,
    val results: List<HTChancedItemResult>,
    override val progressData: HTProgressData,
) : HTItemToChancedItemsRecipe,
    HTProgressRecipe.Simple<SingleRecipeInput> {
    companion object {
        @JvmStatic
        fun <T : HTBasicItemToChancedItemsRecipe> codec(factory: HTItemToChancedItemsRecipeBuilder.Factory<T>): MapCodec<T> = HTCodecs.recordMap { instance ->
            instance.group(
                HTItemIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(HTBasicItemToChancedItemsRecipe::ingredient),
                HTChancedItemResult.CODEC.listOrElement(1, 4).fieldOf(HTConstants.RESULTS).forGetter(HTBasicItemToChancedItemsRecipe::results),
                HTProgressData.CODEC.forGetter(HTBasicItemToChancedItemsRecipe::progressData),
            ).apply(instance, factory::create)
        }

        @JvmField
        val SIMPLE_CODEC: MapCodec<HTBasicItemToChancedItemsRecipe> = codec(::HTBasicItemToChancedItemsRecipe)
    }

    override fun test(input: TypedInstance<Item>): Boolean = ingredient.test(input)

    override fun getRequiredAmount(input: TypedInstance<Item>): Int = ingredient.getRequiredAmount(input)

    override fun assemble(input: ItemInstance): List<ItemStack> = results.map(HTChancedItemResult::createOrEmpty).let(HTResultHelper::mergeStacks)
}
