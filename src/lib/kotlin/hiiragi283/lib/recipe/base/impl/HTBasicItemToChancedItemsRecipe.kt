package hiiragi283.lib.recipe.base.impl

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.base.HTItemToChancedItemsRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.getRequiredAmount
import hiiragi283.lib.recipe.ingredient.test
import hiiragi283.lib.recipe.result.HTChancedItemResult
import hiiragi283.lib.recipe.result.HTResultHelper
import hiiragi283.lib.serialization.codec.listOrElement
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SingleRecipeInput

open class HTBasicItemToChancedItemsRecipe(
    val ingredient: Ingredient,
    val results: List<HTChancedItemResult>,
    override val progressData: HTProgressData,
) : HTItemToChancedItemsRecipe,
    HTProgressRecipe.Simple<SingleRecipeInput> {
    companion object {
        @JvmStatic
        fun <T : HTBasicItemToChancedItemsRecipe> codec(factory: (Ingredient, List<HTChancedItemResult>, HTProgressData) -> T): MapCodec<T> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Ingredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(HTBasicItemToChancedItemsRecipe::ingredient),
                HTChancedItemResult.CODEC.listOrElement(1, 4).fieldOf(HTConstants.RESULTS).forGetter(HTBasicItemToChancedItemsRecipe::results),
                HTProgressData.CODEC.forGetter(HTBasicItemToChancedItemsRecipe::progressData),
            ).apply(instance, factory)
        }

        @JvmField
        val SIMPLE_CODEC: MapCodec<HTBasicItemToChancedItemsRecipe> = codec(::HTBasicItemToChancedItemsRecipe)
    }

    override fun test(input: TypedInstance<Item>): Boolean = ingredient.test(input)

    override fun getRequiredAmount(input: TypedInstance<Item>): Int = ingredient.getRequiredAmount(input)

    override fun assemble(input: ItemInstance): List<ItemStack> = results.map(HTChancedItemResult::createOrEmpty).let(HTResultHelper::mergeStacks)
}
