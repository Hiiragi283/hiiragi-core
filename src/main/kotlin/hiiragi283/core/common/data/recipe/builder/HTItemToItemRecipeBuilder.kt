package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.impl.recipe.HTBasicItemToItemRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.Identifier
import net.neoforged.neoforge.common.crafting.SizedIngredient

class HTItemToItemRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTProcessingRecipeBuilder(prefix) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun charging(output: RecipeOutput, builderAction: HTItemToItemRecipeBuilder.() -> Unit) {
            HTItemToItemRecipeBuilder(HTConst.CHARGING, ::HCChargingRecipe).apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: SizedIngredient
    lateinit var result: HTItemResult

    override fun getPrimalId(): Identifier = result.getId()

    override fun createRecipe(): HTBasicItemToItemRecipe = factory.create(ingredient, result, time)

    //    Factory    //

    fun interface Factory<RECIPE : HTBasicItemToItemRecipe> {
        fun create(ingredient: SizedIngredient, result: HTItemResult, time: Int): RECIPE
    }
}
