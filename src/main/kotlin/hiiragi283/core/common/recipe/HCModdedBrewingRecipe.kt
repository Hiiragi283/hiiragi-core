package hiiragi283.core.common.recipe

import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.recipe.base.HTBrewingRecipe
import hiiragi283.core.api.util.Ior
import hiiragi283.core.util.HCPotionFluidHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStack

class HCModdedBrewingRecipe(val potionFrom: BottledPotionContents, val ingredient: Ingredient, val potionTo: BottledPotionContents) :
    HTBrewingRecipe {
    override fun test(first: ItemStack, second: FluidStack): Boolean =
        ingredient.test(first) && HTPotionHelper.getContents(second) == potionFrom

    override fun assemble(firstInput: ItemStack, secondInput: FluidStack): Ior<ItemStack, FluidStack> =
        Ior.Right(HCPotionFluidHelper.createFluid(potionTo))
}
