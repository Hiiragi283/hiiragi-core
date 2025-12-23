package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.HTCrushingRecipe
import hiiragi283.core.common.recipe.HTDryingRecipe
import hiiragi283.core.common.recipe.HTExplodingRecipe
import hiiragi283.core.common.recipe.HTSingleItemRecipe
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction

class HTSingleItemRecipeBuilder(
    prefix: String,
    private val factory: Factory<*>,
    private val ingredient: HTItemIngredient,
    private val result: HTItemResult,
) : HTProcessingRecipeBuilder<HTSingleItemRecipeBuilder>(prefix) {
    companion object {
        @JvmStatic
        fun crushing(ingredient: HTItemIngredient, result: HTItemResult): HTSingleItemRecipeBuilder =
            HTSingleItemRecipeBuilder(HTConst.CRUSHING, ::HTCrushingRecipe, ingredient, result)

        @JvmStatic
        fun drying(ingredient: HTItemIngredient, result: HTItemResult): HTSingleItemRecipeBuilder =
            HTSingleItemRecipeBuilder(HTConst.DRYING, ::HTDryingRecipe, ingredient, result)

        @JvmStatic
        fun exploding(ingredient: HTItemIngredient, result: HTItemResult): HTSingleItemRecipeBuilder =
            HTSingleItemRecipeBuilder(HTConst.EXPLODING, ::HTExplodingRecipe, ingredient, result)
    }

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTSingleItemRecipe = factory.create(ingredient, result, time, exp)

    fun interface Factory<RECIPE : HTSingleItemRecipe> {
        fun create(
            ingredient: HTItemIngredient,
            result: HTItemResult,
            time: Int,
            exp: Fraction,
        ): RECIPE
    }
}
