package hiiragi283.core.api.recipe.handler

import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.base.HTFluidRecipe
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

@ConsistentCopyVisibility
@JvmRecord
data class HTHandledRecipe<INPUT : RecipeInput, RECIPE : HTRecipe<INPUT>> private constructor(val input: INPUT, val recipe: RECIPE) {
    companion object {
        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : HTRecipe<INPUT>> create(input: INPUT, recipe: RECIPE): HTHandledRecipe<INPUT, RECIPE>? = when {
            recipe.test(input) -> HTHandledRecipe(input, recipe)
            else -> null
        }
    }

    fun assemble(registries: HolderLookup.Provider): ItemStack = recipe.assemble(input, registries)

    inline fun <T> map(transform: (RECIPE, INPUT) -> T): T = transform(recipe, input)
}

//    Extensions    //

fun <INPUT : RecipeInput, RECIPE> HTHandledRecipe<INPUT, RECIPE>.assembleFluid(
    registries: HolderLookup.Provider,
): FluidStack where RECIPE : HTRecipe<INPUT>, RECIPE : HTFluidRecipe<INPUT> = this.map { recipe: RECIPE, input: INPUT ->
    recipe.assembleFluid(input, registries)
}
