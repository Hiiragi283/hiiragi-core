package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.holder.HTIngredientHolder
import hiiragi283.core.api.data.recipe.builder.HTStackRecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.ShapelessRecipe

class HTShapelessRecipeBuilder : HTStackRecipeBuilder(HTConst.SHAPELESS) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTShapelessRecipeBuilder.() -> Unit) {
            HTShapelessRecipeBuilder().apply(builderAction).save(output)
        }
    }

    var group: String? = null
    var category: CraftingBookCategory = CraftingBookCategory.MISC
    var ingredients = HTIngredientHolder.Multiple()

    override fun createRecipe(): ShapelessRecipe = ShapelessRecipe(
        group ?: "",
        category,
        resultStack.stack,
        ingredients.toNonNull(),
    )
}
