package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.progress.HTProgressProvider
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

interface HTItemToFluidRecipe :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<FluidStack>,
    HTProgressProvider<ItemStack>
