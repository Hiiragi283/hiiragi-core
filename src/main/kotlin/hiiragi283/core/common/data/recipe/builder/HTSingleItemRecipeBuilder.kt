package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.HCAnvilCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCSingleItemRecipe
import net.minecraft.resources.ResourceLocation

class HTSingleItemRecipeBuilder(
    prefix: String,
    private val factory: Factory<*>,
    private val ingredient: HTItemIngredient,
    private val result: HTItemResult,
) : HTRecipeBuilder<HTSingleItemRecipeBuilder>(prefix) {
    companion object {
        @JvmStatic
        fun crushing(ingredient: HTItemIngredient, result: HTItemResult): HTSingleItemRecipeBuilder =
            HTSingleItemRecipeBuilder(HTConst.ANVIL_CRUSHING, ::HCAnvilCrushingRecipe, ingredient, result)

        @JvmStatic
        fun exploding(ingredient: HTItemIngredient, result: HTItemResult): HTSingleItemRecipeBuilder =
            HTSingleItemRecipeBuilder(HTConst.EXPLODING, ::HCExplodingRecipe, ingredient, result)
    }

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HCSingleItemRecipe = factory.create(ingredient, result)

    fun interface Factory<RECIPE : HCSingleItemRecipe> {
        fun create(ingredient: HTItemIngredient, result: HTItemResult): RECIPE
    }
}
