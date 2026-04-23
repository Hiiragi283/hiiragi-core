package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HCChargingRecipe(val ingredient: Ingredient, val result: HTItemResult, val requiredEnergy: Int) :
    HTSerializableRecipe<HCChargingRecipe.Input> {
    companion object {
        const val DEFAULT_ENERGY = 1_024_000

        @JvmField
        val CODEC: MapCodec<HCChargingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTCodecs.INGREDIENT.fieldOf(HTConst.INGREDIENT).forGetter(HCChargingRecipe::ingredient),
                    HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCChargingRecipe::result),
                    HTCodecs.NON_NEGATIVE_INT.optionalFieldOf("energy", DEFAULT_ENERGY).forGetter(HCChargingRecipe::requiredEnergy),
                ).apply(instance, ::HCChargingRecipe)
        }
    }

    override fun test(input: Input): Boolean {
        val (item: ItemStack, energy: Int?) = input
        if (!ingredient.test(item)) return false
        return energy == null || energy >= requiredEnergy
    }

    override fun assemble(input: Input, preview: Boolean): ItemStack = result.getOrEmpty(input.energy == null)

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.CHARGING

    override fun getType(): RecipeType<*> = HCRecipeTypes.CHARGING.get()

    @JvmRecord
    data class Input(val item: ItemStack, val energy: Int?) : RecipeInput {
        override fun getItem(index: Int): ItemStack = when (index) {
            0 -> item
            else -> error("No item for index $index")
        }

        override fun size(): Int = 1
    }
}
