package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.api.util.toOption
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
        var ingredient: Ingredient by HTDelegates.onceInitialize()
        var fluidResult: HTFluidResult by HTDelegates.onceInitialize()
        var itemResult: HTItemResult? = null

        override fun getPrimalId(): ResourceLocation = fluidResult.getId()

        override fun createRecipe(): HCTankEmptyingRecipe = HCTankEmptyingRecipe(ingredient, fluidResult, itemResult.toOption())
    }

    class Filling : HTRecipeBuilder(HTConst.FILLING) {
        var itemIngredient: Ingredient by HTDelegates.onceInitialize()
        var fluidIngredient: HTFluidIngredient by HTDelegates.onceInitialize()
        var itemResult: HTItemResult by HTDelegates.onceInitialize()

        override fun getPrimalId(): ResourceLocation = itemResult.getId()

        override fun createRecipe(): HCTankFillingRecipe = HCTankFillingRecipe(itemIngredient, fluidIngredient, itemResult)
    }
}
