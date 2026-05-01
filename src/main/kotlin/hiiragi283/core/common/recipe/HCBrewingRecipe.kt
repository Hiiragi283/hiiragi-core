package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.ingredient.getRequiredAmount
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.util.Ior
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

data class HCBrewingRecipe(val potionFrom: FluidIngredient, val ingredient: Ingredient, val potionTo: HTFluidResult) :
    HTItemOrFluidRecipe,
    HTProgressRecipe.Simple<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HCBrewingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTCodecs.FLUID_INGREDIENT.forGetter(HCBrewingRecipe::potionFrom),
                    HTCodecs.INGREDIENT.fieldOf(HTConst.INGREDIENT).forGetter(HCBrewingRecipe::ingredient),
                    HTFluidResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCBrewingRecipe::potionTo),
                ).apply(instance, ::HCBrewingRecipe)
        }
    }

    override fun test(first: ItemStack, second: FluidStack): Boolean = ingredient.test(first) && potionFrom.test(second)

    override fun getRequiredAmount(first: ItemStack, second: FluidStack): Pair<Int, Int> =
        ingredient.getRequiredAmount(first) to potionFrom.getRequiredAmount(second)

    override fun assemble(input: HTItemAndFluidRecipeInput): Ior<ItemStack, FluidStack> = Ior.Right(potionTo.create())

    override val progressData: HTProgressData = HTProgressData.time(200)
}
