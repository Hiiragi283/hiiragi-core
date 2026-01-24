package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTStackRecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.crafting.SmithingTransformRecipe

class HTSmithingRecipeBuilder : HTStackRecipeBuilder(HTConst.SMITHING) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTSmithingRecipeBuilder.() -> Unit) {
            HTSmithingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    val template: SingleIngredientHolder = SingleIngredientHolder()
    val base: SingleIngredientHolder = SingleIngredientHolder()
    val addition: SingleIngredientHolder = SingleIngredientHolder()

    override fun createRecipe(): SmithingTransformRecipe = SmithingTransformRecipe(
        template.ingredient,
        base.ingredient,
        addition.orEmpty(),
        resultStack.stack,
    )
}
