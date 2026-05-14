package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.result.HTChancedItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HCChargingRecipe(ingredient: Ingredient, result: HTChancedItemResult, val energy: Int) :
    HTInWorldRecipe(ingredient, result),
    HTProgressRecipe<SingleRecipeInput>,
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        const val DEFAULT_ENERGY = 1_024_000

        @JvmField
        val CODEC: MapCodec<HCChargingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    Ingredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(HCChargingRecipe::ingredient),
                    HTChancedItemResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(HCChargingRecipe::result),
                    HTCodecs.NON_NEGATIVE_INT.fieldOf(HTConstants.ENERGY).forGetter(HCChargingRecipe::energy),
                ).apply(instance, ::HCChargingRecipe)
        }
    }

    override fun getProgressData(input: SingleRecipeInput): HTProgressData = HTProgressData.energy(energy)

    override fun getSerializer(): RecipeSerializer<HCChargingRecipe> = HCRecipeSerializers.CHARGING

    override fun getType(): RecipeType<HCChargingRecipe> = HCRecipeTypes.CHARGING.get()
}
