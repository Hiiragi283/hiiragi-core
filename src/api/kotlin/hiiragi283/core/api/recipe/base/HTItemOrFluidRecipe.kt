package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTItemAndFluidResult

interface HTItemOrFluidRecipe :
    HTRecipePredicates.ItemAndFluid,
    HTRecipeFactories.ItemAndFluid<HTItemAndFluidResult>,
    HTProgressRecipe<HTItemAndFluidRecipeInput>
