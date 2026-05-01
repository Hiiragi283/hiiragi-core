package hiiragi283.core.api.recipe.base

import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack

interface HTItemToFluidRecipe :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<FluidStack>,
    HTProgressRecipe<SingleRecipeInput>
