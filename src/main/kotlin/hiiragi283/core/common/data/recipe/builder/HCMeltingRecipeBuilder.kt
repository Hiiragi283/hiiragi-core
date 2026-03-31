package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.registry.holderLike
import hiiragi283.core.common.recipe.HCMeltingRecipe
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

    override fun getPrimalId(): Identifier = result.holderLike().getId()

    override fun createRecipe(): HCMeltingRecipe = HCMeltingRecipe(ingredient, HTFluidResult.create(result), time)
}
