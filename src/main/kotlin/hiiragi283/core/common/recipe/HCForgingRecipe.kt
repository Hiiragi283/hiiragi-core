package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTForgingRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.ingredient.getRequiredAmount
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HCForgingRecipe(
    val primary: HTItemIngredient,
    val secondary: Ingredient,
    val result: HTItemResult,
    override val progressData: HTProgressData,
) : HTForgingRecipe,
    HTProgressRecipe.Simple<HTDoubleRecipeInput>,
    HTSerializableRecipe<HTDoubleRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HCForgingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf("primary").forGetter(HCForgingRecipe::primary),
                    HTCodecs.INGREDIENT.fieldOf("secondary").forGetter(HCForgingRecipe::secondary),
                    HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCForgingRecipe::result),
                    HTProgressData.CODEC.forGetter(HCForgingRecipe::progressData),
                ).apply(instance, ::HCForgingRecipe)
        }
    }

    override fun test(first: ItemStack, second: ItemStack): Boolean = primary.test(first) && secondary.test(second)

    override fun getRequiredAmount(first: ItemStack, second: ItemStack): Pair<Int, Int> =
        primary.getRequiredAmount(first) to secondary.getRequiredAmount(second)

    override fun assemble(input: HTDoubleRecipeInput): ItemStack = result.getOrEmpty()

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.FORGING

    override fun getType(): RecipeType<*> = HCRecipeTypes.FORGING.get()
}
