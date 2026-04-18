package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.HCChargingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

class HCChargingRecipeBuilder : HTRecipeBuilder(HTConst.CHARGING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HCChargingRecipeBuilder.() -> Unit) {
            HCChargingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: Ingredient
    lateinit var result: HTItemResult
    var requiredEnergy: Int = HCChargingRecipe.DEFAULT_ENERGY

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HCChargingRecipe = HCChargingRecipe(ingredient, result, requiredEnergy)
}
