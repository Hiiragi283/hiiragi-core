package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.base.factory.HTItemAndFluidRecipeFactory
import hiiragi283.core.api.recipe.base.predicate.HTDoubleRecipePredicate
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput

interface HTItemOrFluidRecipe :
    HTDoubleRecipePredicate.ItemAndFluid,
    HTItemAndFluidRecipeFactory<HTItemAndFluidRecipeInput>
