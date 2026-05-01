package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.HCCrushingRecipe
import hiiragi283.core.impl.data.recipe.builder.HTMultiOutputRecipeBuilder
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import net.minecraft.data.recipes.RecipeOutput

class HTItemToMultiItemRecipeBuilder(prefix: String, private val factory: Factory<out HTSerializableRecipe<*>>) :
    HTMultiOutputRecipeBuilder(prefix) {
    companion object {
        @JvmStatic
        inline fun crushing(output: RecipeOutput, builderAction: HTItemToMultiItemRecipeBuilder.() -> Unit) {
            HTItemToMultiItemRecipeBuilder(HTConst.CRUSHING, ::HCCrushingRecipe)
                .apply { time /= 2 }
                .apply(builderAction)
                .save(output)
        }
    }

    lateinit var ingredient: HTItemIngredient

    override fun createRecipe(): HTSerializableRecipe<*> = factory.create(ingredient, results, progressData)

    //    Factory    //

    fun interface Factory<T : Any> {
        fun create(ingredient: HTItemIngredient, results: List<HTItemResult>, progressData: HTProgressData): T
    }
}
