package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.Ior

class HTItemOrFluidRecipeBuilder {

    //    Factory    //

    fun interface Factory<RECIPE : Any> {
        fun create(ingredient: Ior<HTItemIngredient, HTFluidIngredient>, result: Ior<HTItemResult, HTFluidResult>, progressData: HTProgressData): RECIPE
    }
}
