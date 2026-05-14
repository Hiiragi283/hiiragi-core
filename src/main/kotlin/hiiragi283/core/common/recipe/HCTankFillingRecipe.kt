package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.recipe.HTTankFillingRecipe
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.getRequiredAmount
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack

class HCTankFillingRecipe(val itemIngredient: Ingredient, val fluidIngredient: HTFluidIngredient, val result: HTItemResult) :
    HTTankFillingRecipe,
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HCTankFillingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    Ingredient.CODEC.fieldOf(HTConstants.ITEM_INGREDIENT).forGetter(HCTankFillingRecipe::itemIngredient),
                    HTFluidIngredient.CODEC.fieldOf(HTConstants.FLUID_INGREDIENT).forGetter(HCTankFillingRecipe::fluidIngredient),
                    HTItemResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(HCTankFillingRecipe::result),
                ).apply(instance, ::HCTankFillingRecipe)
        }
    }

    override fun testContainer(stack: ItemStack): Boolean = itemIngredient.test(stack)

    override fun testFluid(stack: FluidStack): Boolean = fluidIngredient.test(stack)

    override fun assemble(firstInput: ItemStack, secondInput: FluidStack): ItemStack = result.createOrEmpty()

    override fun getRequiredAmount(first: ItemStack, second: FluidStack): Pair<Int, Int> = itemIngredient.getRequiredAmount(first) to fluidIngredient.getRequiredAmount(second)

    override fun getSerializer(): RecipeSerializer<HCTankFillingRecipe> = HCRecipeSerializers.FILLING

    override fun getType(): RecipeType<HCTankFillingRecipe> = HCRecipeTypes.FILLING.get()
}
