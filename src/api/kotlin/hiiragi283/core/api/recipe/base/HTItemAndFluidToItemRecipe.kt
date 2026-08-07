package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.progress.HTBiProgressProvider
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

interface HTItemAndFluidToItemRecipe :
    HTRecipePredicates.ItemAndFluid,
    HTRecipeFactories.ItemAndFluid<ItemStack>,
    HTBiProgressProvider<ItemStack, FluidStack>
