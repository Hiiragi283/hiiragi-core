package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.fraction
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.HCExplodingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction

class HCExplodingRecipeBuilder : HTRecipeBuilder(HTConst.EXPLODING) {
    companion object {
        @JvmStatic
        fun create(output: RecipeOutput, builderAction: HCExplodingRecipeBuilder.() -> Unit) {
            HCExplodingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: HTItemIngredient
    lateinit var result: HTItemResult
    var minPower: Fraction = fraction(4f)

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HCExplodingRecipe = HCExplodingRecipe(
        ingredient,
        result,
        minPower,
    )
}
