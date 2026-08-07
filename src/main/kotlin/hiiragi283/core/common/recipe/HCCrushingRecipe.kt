package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.recipe.HTSerializableRecipe
import hiiragi283.core.api.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.progress.HTProgressData
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.core.support.recipe.base.HTBasicItemToMultiItemRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HCCrushingRecipe(ingredient: HTItemIngredient, results: List<HTChancedItemResult>, progressData: HTProgressData) :
    HTBasicItemToMultiItemRecipe(ingredient, results, progressData),
    HTItemToMultiItemRecipe,
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HCCrushingRecipe> = codec(4, ::HCCrushingRecipe)
    }

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.CRUSHING

    override fun getType(): RecipeType<*> = HCRecipeTypes.CRUSHING
}
