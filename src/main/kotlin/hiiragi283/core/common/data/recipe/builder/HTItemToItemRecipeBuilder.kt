package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.base.HTBasicItemToItemRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTItemToItemRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTProcessingRecipeBuilder(prefix) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun charging(output: RecipeOutput, builderAction: HTItemToItemRecipeBuilder.() -> Unit) {
            HTItemToItemRecipeBuilder(HTConst.CHARGING, ::HCChargingRecipe).apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: HTItemIngredient
    lateinit var result: HTItemResult

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTBasicItemToItemRecipe = factory.create(ingredient, result, time)

    //    Factory    //

    fun interface Factory<RECIPE : HTBasicItemToItemRecipe> {
        fun create(ingredient: HTItemIngredient, result: HTItemResult, time: Int): RECIPE
    }
}
