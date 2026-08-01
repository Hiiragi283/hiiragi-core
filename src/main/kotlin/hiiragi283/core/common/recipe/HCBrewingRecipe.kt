package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTSerializableRecipe
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemAndFluidResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class HCBrewingRecipe(
    val itemIngredient: HTItemIngredient,
    val fluidIngredient: HTFluidIngredient,
    val result: HTFluidResult,
    override val progressData: HTProgressData,
) : HTItemOrFluidRecipe,
    HTSerializableRecipe<HTItemAndFluidRecipeInput>,
    HTProgressRecipe.Simple<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HCBrewingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTItemIngredient.CODEC.fieldOf(HTConst.ITEM_INGREDIENT).forGetter(HCBrewingRecipe::itemIngredient),
                HTFluidIngredient.CODEC.fieldOf(HTConst.FLUID_INGREDIENT).forGetter(HCBrewingRecipe::fluidIngredient),
                HTFluidResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCBrewingRecipe::result),
                HTProgressData.CODEC.forGetter(HCBrewingRecipe::progressData),
            ).apply(instance, ::HCBrewingRecipe)
        }
    }

    override fun test(first: ItemStack, second: FluidStack): Boolean = itemIngredient.test(first) && fluidIngredient.test(second)

    override fun getMatchingStacks(first: ItemStack, second: FluidStack): Pair<ItemStack, FluidStack> = itemIngredient.getMatchingStack(first) to fluidIngredient.getMatchingStack(second)

    override fun isIncomplete(): Boolean = fluidIngredient.isIncomplete() || itemIngredient.isIncomplete()

    override fun assemble(firstInput: ItemStack, secondInput: FluidStack): HTItemAndFluidResult = HTItemAndFluidResult(result.create())

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.BREWING

    override fun getType(): RecipeType<*> = HCRecipeTypes.BREWING
}
