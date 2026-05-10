package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import net.minecraft.world.item.ItemStack

interface HTItemAndFluidToItemRecipe :
    HTRecipePredicates.ItemAndFluid,
    HTRecipeFactories.ItemAndFluid<ItemStack>,
    HTProgressRecipe<HTItemAndFluidRecipeInput>
