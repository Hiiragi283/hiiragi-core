package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.base.HTFluidRecipe
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

data class HCBrewingRecipe(val potionFrom: SizedFluidIngredient, val ingredient: SizedIngredient, val potionTo: HTFluidResult) :
    HTProcessingRecipe<HTItemAndFluidRecipeInput>,
    HTFluidRecipe<HTItemAndFluidRecipeInput> {
    override val time: Int = 100

    override fun test(input: HTItemAndFluidRecipeInput): Boolean = potionFrom.test(input.fluid) && ingredient.test(input.item)

    override fun assemble(input: HTItemAndFluidRecipeInput): ItemStack = ItemStack.EMPTY

    override fun assembleFluid(input: HTItemAndFluidRecipeInput): FluidStack = potionTo.create()
}
