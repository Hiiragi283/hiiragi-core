package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.wrapOptional
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

data object HTTankInteractionRecipeBuilder {
    @JvmStatic
    inline fun emptying(output: RecipeOutput, builderAction: Emptying.() -> Unit) {
        Emptying().apply(builderAction).save(output)
    }

    @JvmStatic
    inline fun filling(output: RecipeOutput, builderAction: Filling.() -> Unit) {
        Filling().apply(builderAction).save(output)
    }

    class Emptying : HTRecipeBuilder(HTConst.EMPTYING) {
        lateinit var ingredient: Ingredient
        lateinit var fluidResult: HTFluidResult
        var itemResult: HTItemResult? = null

        override fun getPrimalId(): ResourceLocation = fluidResult.getId()

        override fun createRecipe(): HCTankEmptyingRecipe = HCTankEmptyingRecipe(ingredient, fluidResult, itemResult.wrapOptional())
    }

    class Filling : HTRecipeBuilder(HTConst.FILLING) {
        lateinit var itemIngredient: Ingredient
        lateinit var fluidIngredient: HTFluidIngredient
        lateinit var itemResult: HTItemResult

        override fun getPrimalId(): ResourceLocation = itemResult.getId()

        override fun createRecipe(): HCTankFillingRecipe = HCTankFillingRecipe(itemIngredient, fluidIngredient, itemResult)
    }
}
