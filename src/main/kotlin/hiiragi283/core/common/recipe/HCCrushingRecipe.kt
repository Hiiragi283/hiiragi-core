package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.recipe.base.HTCrushingRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.impl.recipe.HTBasicItemToMultiItemRecipe
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HCCrushingRecipe(ingredient: HTItemIngredient, results: List<HTItemResult>, progressData: HTProgressData) :
    HTBasicItemToMultiItemRecipe(ingredient, results, progressData),
    HTSerializableRecipe<SingleRecipeInput>,
    HTCrushingRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HCCrushingRecipe> = codec(1..4, ::HCCrushingRecipe)
    }

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.CRUSHING

    override fun getType(): RecipeType<*> = HCRecipeTypes.CRUSHING.get()
}
