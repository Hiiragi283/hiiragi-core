package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.holder.HTIngredientHolder
import hiiragi283.core.impl.data.recipe.builder.HTStackRecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.crafting.SmithingTransformRecipe

class HTSmithingRecipeBuilder : HTStackRecipeBuilder(HTConst.SMITHING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTSmithingRecipeBuilder.() -> Unit) {
            HTSmithingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    val template = HTIngredientHolder.Single()
    val base = HTIngredientHolder.Single()
    val addition = HTIngredientHolder.Single()

    override fun createRecipe(): SmithingTransformRecipe = SmithingTransformRecipe(
        template.ingredient,
        base.ingredient,
        addition.orEmpty(),
        resultStack.stack,
    )
}
