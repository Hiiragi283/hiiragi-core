package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.HCChargingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HCChargingRecipeBuilder : HTRecipeBuilder(HTConst.CHARGING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HCChargingRecipeBuilder.() -> Unit) {
            HCChargingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: HTItemIngredient
    lateinit var result: HTItemResult
    var requiredEnergy: Int = HCChargingRecipe.DEFAULT_ENERGY

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HCChargingRecipe = HCChargingRecipe(ingredient, result, requiredEnergy)
}
