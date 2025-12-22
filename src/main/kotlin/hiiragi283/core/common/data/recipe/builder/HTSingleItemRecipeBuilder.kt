package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.math.toFraction
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.HTCrushingRecipe
import hiiragi283.core.common.recipe.HTDryingRecipe
import hiiragi283.core.common.recipe.HTExplodingRecipe
import hiiragi283.core.common.recipe.HTSingleItemRecipe
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction
import kotlin.math.max

class HTSingleItemRecipeBuilder(
    prefix: String,
    private val factory: Factory<*>,
    private val ingredient: HTItemIngredient,
    private val result: HTItemResult,
) : HTRecipeBuilder<HTSingleItemRecipeBuilder>(prefix) {
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

    private var time: Int = 20 * 10
    private var exp: Fraction = Fraction.ZERO

    fun setTime(time: Int): HTSingleItemRecipeBuilder = apply {
        this.time = max(0, time)
    }

    fun setExp(exp: Float): HTSingleItemRecipeBuilder = setExp(exp.toFraction())

    fun setExp(exp: Fraction): HTSingleItemRecipeBuilder = apply {
        this.exp = maxOf(Fraction.ZERO, exp)
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
