package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTTankFillingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.getRequiredAmount
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
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
                    HTCodecs.INGREDIENT.fieldOf(HTConst.ITEM_INGREDIENT).forGetter(HCTankFillingRecipe::itemIngredient),
                    HTFluidIngredient.CODEC.fieldOf(HTConst.FLUID_INGREDIENT).forGetter(HCTankFillingRecipe::fluidIngredient),
                    HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCTankFillingRecipe::result),
                ).apply(instance, ::HCTankFillingRecipe)
        }
    }

    override fun testContainer(stack: ItemStack): Boolean = itemIngredient.test(stack)

    override fun testFluid(stack: FluidStack): Boolean = fluidIngredient.test(stack)

    override fun assemble(firstInput: ItemStack, secondInput: FluidStack): ItemStack = result.getOrEmpty()

    override fun getRequiredAmount(first: ItemStack, second: FluidStack): Pair<Int, Int> =
        itemIngredient.getRequiredAmount(first) to fluidIngredient.getRequiredAmount(second)

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.FILLING

    override fun getType(): RecipeType<*> = HCRecipeTypes.FILLING.get()
}
