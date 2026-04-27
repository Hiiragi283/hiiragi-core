package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.base.HTSingleItemRecipe
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HCChargingRecipe(val ingredient: Ingredient, val result: HTItemResult, override val energy: Int) :
    HTSingleItemRecipe.Serializable,
    HTProgressRecipe.Energized<SingleRecipeInput> {
    companion object {
        const val DEFAULT_ENERGY = 1_024_000

        @JvmField
        val CODEC: MapCodec<HCChargingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTCodecs.INGREDIENT.fieldOf(HTConst.INGREDIENT).forGetter(HCChargingRecipe::ingredient),
                    HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCChargingRecipe::result),
                    HTProgressRecipe.energyCodec(),
                ).apply(instance, ::HCChargingRecipe)
        }
    }

    override fun getRequiredAmount(input: SingleRecipeInput): Int = when {
        ingredient.test(input.item()) -> 1
        else -> 0
    }

    override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    override fun assemble(input: SingleRecipeInput, preview: Boolean): ItemStack = result.getOrEmpty(preview)

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.CHARGING

    override fun getType(): RecipeType<*> = HCRecipeTypes.CHARGING.get()
}
