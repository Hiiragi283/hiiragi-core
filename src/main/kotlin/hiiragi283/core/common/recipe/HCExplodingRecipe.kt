package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.fraction
import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import org.apache.commons.lang3.math.Fraction

class HCExplodingRecipe(val ingredient: Ingredient, val result: HTItemResult, val requiredPower: Fraction) :
    HTSerializableRecipe<HCExplodingRecipe.Input> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HCExplodingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTCodecs.INGREDIENT.fieldOf(HTConst.INGREDIENT).forGetter(HCExplodingRecipe::ingredient),
                    HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCExplodingRecipe::result),
                    HTCodecs.NON_NEGATIVE_FRACTION
                        .optionalFieldOf(
                            "required_power",
                            fraction(4),
                        ).forGetter(HCExplodingRecipe::requiredPower),
                ).apply(instance, ::HCExplodingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HCExplodingRecipe> = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            HCExplodingRecipe::ingredient,
            HTItemResult.STREAM_CODEC,
            HCExplodingRecipe::result,
            HTStreamCodecs.FRACTION,
            HCExplodingRecipe::requiredPower,
            ::HCExplodingRecipe,
        )
    }

    override fun test(input: Input): Boolean = ingredient.test(input.item) && input.power >= requiredPower

    override fun assemble(input: Input, preview: Boolean): ItemStack = result.getOrEmpty(preview)

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.EXPLODING

    override fun getType(): RecipeType<*> = HCRecipeTypes.EXPLODING.get()

    @JvmRecord
    data class Input(val item: ItemStack, val power: Fraction) : RecipeInput {
        override fun getItem(index: Int): ItemStack = item

        override fun size(): Int = 1
    }
}
