package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.HCLightningChargingRecipe
import hiiragi283.core.common.recipe.HCSingleItemRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe

class HCSingleItemRecipeBuilder(private val factory: Factory<*>, prefix: String) : HTRecipeBuilder(prefix) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        fun charging(output: RecipeOutput, builderAction: HCSingleItemRecipeBuilder.() -> Unit) {
            HCSingleItemRecipeBuilder(::HCLightningChargingRecipe, HTConst.CHARGING)
                .apply(builderAction)
                .save(output)
        }
    }

    lateinit var ingredient: HTItemIngredient
    lateinit var result: HTItemResult

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): Recipe<*> = factory.create(ingredient, result)

    fun interface Factory<RECIPE : HCSingleItemRecipe<*>> {
        fun create(ingredient: HTItemIngredient, result: HTItemResult): RECIPE
    }
}
