package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.HCCrushingRecipe
import hiiragi283.core.impl.data.recipe.builder.HTChancedRecipeBuilder
import hiiragi283.core.impl.recipe.HTBasicItemToChancedRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.neoforged.neoforge.common.crafting.SizedIngredient
import java.util.Optional

class HTItemToChancedRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTChancedRecipeBuilder(prefix) {
    companion object {
        @JvmStatic
        inline fun crushing(output: RecipeOutput, builderAction: HTItemToChancedRecipeBuilder.() -> Unit) {
            HTItemToChancedRecipeBuilder(HTConst.CRUSHING, ::HCCrushingRecipe).apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: SizedIngredient

    init {
        time /= 2
    }

    override fun createRecipe(): HTBasicItemToChancedRecipe =
        factory.create(ingredient, HTItemResult(result), extraResult.toOptional(), time)

    //    Factory    //

    fun interface Factory<RECIPE : HTBasicItemToChancedRecipe> {
        fun create(
            ingredient: SizedIngredient,
            result: HTItemResult,
            extraResult: Optional<HTItemResult>,
            time: Int,
        ): RECIPE
    }
}
