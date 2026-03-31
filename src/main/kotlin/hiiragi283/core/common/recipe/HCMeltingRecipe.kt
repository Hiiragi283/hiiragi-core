package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.base.HTFluidRecipe
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.setup.HCRecipeBookCategories
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeBookCategory
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack

class HCMeltingRecipe(val ingredient: Ingredient, val result: HTFluidResult, override val time: Int) :
    HTProcessingRecipe.Serializable<SingleRecipeInput>,
    HTFluidRecipe<SingleRecipeInput> {
    override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    override fun assemble(input: SingleRecipeInput): ItemStack = ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<HCMeltingRecipe> = HCRecipeSerializers.MELTING

    override fun getType(): RecipeType<HCMeltingRecipe> = HCRecipeTypes.MELTING.get()

    override fun recipeBookCategory(): RecipeBookCategory = HCRecipeBookCategories.MELTING

    override fun assembleFluid(input: SingleRecipeInput): FluidStack = result.create()
}
