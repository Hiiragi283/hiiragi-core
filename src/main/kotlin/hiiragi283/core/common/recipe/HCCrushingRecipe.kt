package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.impl.HTBasicItemToChancedItemsRecipe
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTChancedItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HCCrushingRecipe(ingredient: HTItemIngredient, results: List<HTChancedItemResult>, progressData: HTProgressData) :
    HTBasicItemToChancedItemsRecipe(ingredient, results, progressData),
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HCCrushingRecipe> = codec(::HCCrushingRecipe)
    }

    override fun getSerializer(): RecipeSerializer<HCCrushingRecipe> = HCRecipeSerializers.CRUSHING

    override fun getType(): RecipeType<HCCrushingRecipe> = HCRecipeTypes.CRUSHING
}
