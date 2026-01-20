package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.fraction
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.HCAnvilCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCLightningChargingRecipe
import hiiragi283.core.common.recipe.HCSingleItemRecipe
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction

class HTSingleItemRecipeBuilder(prefix: String, private val recipe: HCSingleItemRecipe<*>, private val id: ResourceLocation) :
    HTRecipeBuilder<HTSingleItemRecipeBuilder>(prefix) {
    companion object {
        @JvmStatic
        fun charging(ingredient: HTItemIngredient, result: HTItemResult): HTSingleItemRecipeBuilder =
            HTSingleItemRecipeBuilder(HTConst.CHARGING, HCLightningChargingRecipe(ingredient, result), result.getId())

        @JvmStatic
        fun crushing(ingredient: HTItemIngredient, result: HTItemResult): HTSingleItemRecipeBuilder =
            HTSingleItemRecipeBuilder(HTConst.ANVIL_CRUSHING, HCAnvilCrushingRecipe(ingredient, result), result.getId())

        @JvmStatic
        fun exploding(ingredient: HTItemIngredient, result: HTItemResult, minPowder: Float = 4f): HTSingleItemRecipeBuilder =
            exploding(ingredient, result, fraction(minPowder))

        @JvmStatic
        fun exploding(ingredient: HTItemIngredient, result: HTItemResult, minPowder: Fraction): HTSingleItemRecipeBuilder =
            HTSingleItemRecipeBuilder(HTConst.EXPLODING, HCExplodingRecipe(ingredient, result, minPowder), result.getId())
    }

    override fun getPrimalId(): ResourceLocation = id

    override fun createRecipe(): HCSingleItemRecipe<*> = recipe
}
