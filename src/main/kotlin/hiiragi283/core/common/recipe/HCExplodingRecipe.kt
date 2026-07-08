package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.result.HTChancedItemResult
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HCExplodingRecipe(ingredient: Ingredient, result: HTChancedItemResult) :
    HTInWorldRecipe(ingredient, result),
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HCExplodingRecipe> = codec(::HCExplodingRecipe)
    }

    override fun getSerializer(): RecipeSerializer<HCExplodingRecipe> = HCRecipeSerializers.EXPLODING

    override fun getType(): RecipeType<HCExplodingRecipe> = HCRecipeTypes.EXPLODING
}
