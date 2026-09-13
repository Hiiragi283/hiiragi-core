package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTSerializableRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.core.support.recipe.base.HTInWorldRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HCChargingRecipe(ingredient: HTItemIngredient, result: HTChancedItemResult, val energy: Int) :
    HTInWorldRecipe(ingredient, result),
    HTProgressRecipe.Simple<SingleRecipeInput>,
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        const val DEFAULT_ENERGY = 1_024_000

        @JvmField
        val CODEC: MapCodec<HCChargingRecipe> = HTCodecs.recordMap { instance ->
            instance
                .group(
                    HTItemIngredient.SINGLE_CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HCChargingRecipe::ingredient),
                    HTChancedItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCChargingRecipe::result),
                    HTCodecs.NON_NEGATIVE_INT.fieldOf(HTConst.ENERGY).forGetter(HCChargingRecipe::energy),
                ).apply(instance, ::HCChargingRecipe)
        }
    }

    override val progressData: HTProgressData get() = HTProgressData.energy(energy)

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.CHARGING

    override fun getType(): RecipeType<*> = HCRecipeTypes.CHARGING
}
