package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.base.HTTankFillingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack

class HCTankFillingRecipe(val itemIngredient: Ingredient, val fluidIngredient: HTFluidIngredient, val result: HTItemResult) :
    HTTankFillingRecipe.Serializable {
    override fun testContainer(stack: ItemStack): Boolean = itemIngredient.test(stack)

    override fun testFluid(stack: FluidStack): Boolean = fluidIngredient.test(stack)

    override fun getRequiredFluidAmount(input: HTItemAndFluidRecipeInput): Int = fluidIngredient.amount

    override fun assemble(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): ItemStack =
        result.getStackOrEmpty(registries)

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.FILLING

    override fun getType(): RecipeType<*> = HCRecipeTypes.FILLING.get()
}
