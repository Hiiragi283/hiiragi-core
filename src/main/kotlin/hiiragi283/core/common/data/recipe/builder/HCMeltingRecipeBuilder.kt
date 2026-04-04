package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.registry.holderLike
import hiiragi283.core.common.recipe.HCMeltingRecipe
import net.minecraft.advancements.criterion.MinMaxBounds
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStackTemplate

class HCMeltingRecipeBuilder : HTProcessingRecipeBuilder(HTConst.MELTING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HCMeltingRecipeBuilder.() -> Unit) {
            HCMeltingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: Ingredient
    lateinit var result: FluidStackTemplate
    var heatRange: MinMaxBounds.Ints = heatRange(100)

    fun heatRange(min: Int): MinMaxBounds.Ints {
        val kelvinTemp: Int = 300 + min
        check(kelvinTemp >= 0) { "Minimum temperature must not be negative" }
        return MinMaxBounds.Ints.atLeast(kelvinTemp)
    }

    override fun getPrimalId(): Identifier = result.holderLike().getId()

    override fun createRecipe(): HCMeltingRecipe = HCMeltingRecipe(ingredient, HTFluidResult.create(result), heatRange, time)
}
