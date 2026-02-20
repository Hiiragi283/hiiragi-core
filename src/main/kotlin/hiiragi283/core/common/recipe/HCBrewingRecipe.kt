package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.HTFluidRecipe
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack

class HCBrewingRecipe(val potionFrom: HTFluidIngredient, val ingredient: HTItemIngredient, val potionTo: HTFluidResult) :
    HTProcessingRecipe<HTItemAndFluidRecipeInput>,
    HTFluidRecipe<HTItemAndFluidRecipeInput> {
    override val time: Int = 100

    override fun assembleFluid(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): FluidStack =
        potionTo.getStackOrEmpty(registries)

    override fun matches(input: HTItemAndFluidRecipeInput, level: Level): Boolean =
        potionFrom.test(input.fluid) && ingredient.test(input.item)

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = throw UnsupportedOperationException()

    override fun getType(): RecipeType<*> = throw UnsupportedOperationException()
}
