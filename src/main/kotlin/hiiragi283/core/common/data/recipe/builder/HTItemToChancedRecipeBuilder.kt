package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.HTCrushingRecipe
import hiiragi283.core.common.recipe.base.HTBasicItemToChancedRecipe
import net.minecraft.data.recipes.RecipeOutput
import java.util.Optional

class HTItemToChancedRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTChancedRecipeBuilder(prefix) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun crushing(output: RecipeOutput, builderAction: HTItemToChancedRecipeBuilder.() -> Unit) {
            HTItemToChancedRecipeBuilder(HTConst.CRUSHING, ::HTCrushingRecipe).apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: HTItemIngredient

    init {
        time /= 2
    }

    override fun createRecipe(): HTBasicItemToChancedRecipe = factory.create(ingredient, result, extraResult.toOptional(), time)

    //    Factory    //

    fun interface Factory<RECIPE : HTBasicItemToChancedRecipe> {
        fun create(
            ingredient: HTItemIngredient,
            result: HTItemResult,
            extraResult: Optional<HTChancedItemResult>,
            time: Int,
        ): RECIPE
    }
}
