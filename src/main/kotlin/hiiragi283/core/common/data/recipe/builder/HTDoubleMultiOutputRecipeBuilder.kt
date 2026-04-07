package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.wrapOptional
import hiiragi283.core.common.recipe.HCForgingRecipe
import hiiragi283.core.impl.data.recipe.builder.HTMultiOutputRecipeBuilder
import hiiragi283.core.impl.recipe.HTBasicDoubleMultiOutputRecipe
import net.minecraft.data.recipes.RecipeOutput
import java.util.Optional

class HTDoubleMultiOutputRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTMultiOutputRecipeBuilder(prefix) {
    companion object {
        @JvmStatic
        inline fun forging(output: RecipeOutput, builderAction: HTDoubleMultiOutputRecipeBuilder.() -> Unit) {
            HTDoubleMultiOutputRecipeBuilder(HTConst.FORGING, ::HCForgingRecipe).apply(builderAction).save(output)
        }
    }

    lateinit var base: HTItemIngredient
    var addition: HTItemIngredient? = null

    init {
        time /= 2
    }

    override fun createRecipe(): HTBasicDoubleMultiOutputRecipe = factory.create(base, addition.wrapOptional(), results, time)

    //    Factory    //

    fun interface Factory<RECIPE : HTBasicDoubleMultiOutputRecipe> {
        fun create(
            base: HTItemIngredient,
            addition: Optional<HTItemIngredient>,
            results: List<HTItemResult>,
            time: Int,
        ): RECIPE
    }
}
