package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.resources.ResourceLocation

class HTSingleItemRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTProcessingRecipeBuilder(prefix) {
    lateinit var ingredient: HTItemIngredient
    lateinit var result: HTItemResult

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTSerializableRecipe<*> = factory.create(ingredient, result, time)

    //    Factory    //

    fun interface Factory<RECIPE : HTSerializableRecipe<*>> {
        fun create(ingredient: HTItemIngredient, result: HTItemResult, time: Int): RECIPE
    }
}
