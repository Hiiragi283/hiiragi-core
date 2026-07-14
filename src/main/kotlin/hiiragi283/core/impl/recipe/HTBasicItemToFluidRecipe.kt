package hiiragi283.core.impl.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTItemToFluidRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.common.data.recipe.builder.HTItemToResultRecipeBuilder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack

open class HTBasicItemToFluidRecipe(
    val ingredient: HTItemIngredient,
    val result: HTFluidResult,
    override val progressData: HTProgressData,
) : HTItemToFluidRecipe,
    HTProgressRecipe.Simple<SingleRecipeInput> {
    companion object {
        @JvmStatic
        fun <T : HTBasicItemToFluidRecipe> codec(factory: HTItemToResultRecipeBuilder.Factory<HTFluidResult, T>): MapCodec<T> = HTCodecs.recordMap { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTBasicItemToFluidRecipe::ingredient),
                    HTFluidResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTBasicItemToFluidRecipe::result),
                    HTProgressData.CODEC.forGetter(HTBasicItemToFluidRecipe::progressData),
                ).apply(instance, factory::create)
        }

        @JvmField
        val SIMPLE_CODEC: MapCodec<HTBasicItemToFluidRecipe> = codec(::HTBasicItemToFluidRecipe)
    }

    override fun test(input: ItemStack): Boolean = ingredient.test(input)

    override fun getRequiredAmount(input: ItemStack): Int = ingredient.getRequiredAmount(input)

    override fun assemble(input: ItemStack): FluidStack = result.create()
}
