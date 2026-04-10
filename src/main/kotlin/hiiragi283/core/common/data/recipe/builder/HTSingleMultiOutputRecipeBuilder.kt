package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.HCCrushingRecipe
import hiiragi283.core.impl.data.recipe.builder.HTMultiOutputRecipeBuilder
import net.minecraft.data.recipes.RecipeOutput

class HTSingleMultiOutputRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTMultiOutputRecipeBuilder(prefix) {
    companion object {
        @JvmStatic
        inline fun crushing(output: RecipeOutput, builderAction: HTSingleMultiOutputRecipeBuilder.() -> Unit) {
            HTSingleMultiOutputRecipeBuilder(HTConst.CRUSHING, ::HCCrushingRecipe).apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: HTItemIngredient

    init {
        time /= 2
    }

    override fun createRecipe(): HTSerializableRecipe<*> = factory.create(ingredient, results, time)

    //    Factory    //

    fun interface Factory<RECIPE : HTSerializableRecipe<*>> {
        fun create(ingredient: HTItemIngredient, results: List<HTItemResult>, time: Int): RECIPE
    }
}
