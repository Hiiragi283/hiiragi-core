package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.base.FluidAmount
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.ItemAmount
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.util.Ior
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Predicate

data class HCBrewingRecipe(val potionFrom: HTFluidIngredient, val ingredient: Ingredient, val potionTo: HTFluidResult) :
    HTItemOrFluidRecipe {
    override fun getPredicate(): Ior<Predicate<ItemStack>, Predicate<FluidStack>> = Ior.Both(ingredient, Predicate(potionFrom::test))

    override fun getRequiredAmount(input: HTItemAndFluidRecipeInput): Ior<ItemAmount, FluidAmount> = Ior.Both(1, potionFrom.amount)

    override val time: Int = 100

    override fun assemble(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    override fun assembleFluid(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): FluidStack =
        potionTo.getStackOrEmpty(registries)
}
