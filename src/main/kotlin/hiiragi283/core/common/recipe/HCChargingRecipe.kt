package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.impl.recipe.HTInWorldRecipe
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HCChargingRecipe(ingredient: Ingredient, result: HTItemResult, val energy: Int) :
    HTInWorldRecipe(ingredient, result),
    HTProgressRecipe<SingleRecipeInput>,
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        const val DEFAULT_ENERGY = 1_024_000

        @JvmField
        val CODEC: MapCodec<HCChargingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTCodecs.INGREDIENT.fieldOf(HTConst.INGREDIENT).forGetter(HCChargingRecipe::ingredient),
                    HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCChargingRecipe::result),
                    HTCodecs.NON_NEGATIVE_INT.fieldOf(HTConst.ENERGY).forGetter(HCChargingRecipe::energy),
                ).apply(instance, ::HCChargingRecipe)
        }
    }

    override fun getProgressData(input: SingleRecipeInput): HTProgressData = HTProgressData.energy(energy)

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.CHARGING

    override fun getType(): RecipeType<*> = HCRecipeTypes.CHARGING.get()
}
