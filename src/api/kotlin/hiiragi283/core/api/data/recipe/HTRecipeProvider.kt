package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.data.recipe.ingredient.HTFluidIngredientCreator
import hiiragi283.core.api.data.recipe.ingredient.HTIngredientAccess
import hiiragi283.core.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.core.api.data.recipe.result.HTResultCreator
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.world.level.material.Fluid

abstract class HTRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput) : RecipeProvider(registries, output) {
    protected val fluids: HolderGetter<Fluid> = registries.lookupOrThrow(Registries.FLUID)

    protected val itemCreator: HTItemIngredientCreator = HTIngredientAccess.INSTANCE.itemCreator(items)
    protected val fluidCreator: HTFluidIngredientCreator = HTIngredientAccess.INSTANCE.fluidCreator(fluids)

    protected val resultCreator: HTResultCreator = HTResultCreator
}
