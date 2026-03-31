package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.impl.data.recipe.builder.HTAbstractSingleItemRecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.crafting.Ingredient

class HCChargingRecipeBuilder : HTAbstractSingleItemRecipeBuilder(HTConst.CHARGING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HCChargingRecipeBuilder.() -> Unit) {
            HCChargingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: Ingredient
    var chance: Float = 1f
    var energy: Int = HCChargingRecipe.DEFAULT_ENERGY

    override fun createRecipe(): HCChargingRecipe = HCChargingRecipe(ingredient, result.createResult(chance), energy)
}
