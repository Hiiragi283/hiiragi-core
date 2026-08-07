package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.progress.HTBiProgressProvider
import hiiragi283.core.api.recipe.result.HTItemAndFluidResult
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

interface HTItemOrFluidRecipe :
    HTRecipePredicates.ItemAndFluid,
    HTRecipeFactories.ItemAndFluid<HTItemAndFluidResult>,
    HTBiProgressProvider<ItemStack, FluidStack>
