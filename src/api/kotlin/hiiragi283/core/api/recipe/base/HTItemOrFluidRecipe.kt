package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.util.Ior
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

interface HTItemOrFluidRecipe :
    HTRecipePredicates.ItemAndFluid,
    HTRecipeFactories.ItemAndFluid<Ior<ItemStack, FluidStack>>
