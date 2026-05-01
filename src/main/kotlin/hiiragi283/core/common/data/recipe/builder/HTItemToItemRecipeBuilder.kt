package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe

class HTItemToItemRecipeBuilder(prefix: String, private val factory: Factory<out HTSerializableRecipe<*>>) :
    HTProgressRecipeBuilder(prefix) {
    lateinit var ingredient: HTItemIngredient
    lateinit var result: HTItemResult

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): Recipe<*> = factory.create(ingredient, result, progressData)

    //    Factory    //

    fun interface Factory<RECIPE : Any> {
        fun create(ingredient: HTItemIngredient, result: HTItemResult, progressData: HTProgressData): RECIPE
    }
}
