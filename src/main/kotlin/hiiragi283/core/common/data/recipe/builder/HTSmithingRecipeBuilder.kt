package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.impl.data.recipe.builder.HTStackRecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SmithingTransformRecipe

class HTSmithingRecipeBuilder : HTStackRecipeBuilder(HTConst.SMITHING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTSmithingRecipeBuilder.() -> Unit) {
            HTSmithingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    var template: Ingredient by HTDelegates.onceInitialize()
    var base: Ingredient by HTDelegates.onceInitialize()
    var addition: Ingredient = Ingredient.EMPTY

    override fun createRecipe(): SmithingTransformRecipe = SmithingTransformRecipe(
        template,
        base,
        addition,
        resultStack.stack,
    )
}
