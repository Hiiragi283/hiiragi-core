package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.impl.data.recipe.builder.HTSingleItemRecipeBuilder
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapelessRecipe

/**
 * @see net.minecraft.data.recipes.ShapelessRecipeBuilder
 */
class HTShapelessRecipeBuilder : HTSingleItemRecipeBuilder(HTConst.SHAPELESS) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTShapelessRecipeBuilder.() -> Unit) {
            HTShapelessRecipeBuilder().apply(builderAction).save(output)
        }
    }

    var group: String? = null
    var category: RecipeCategory = RecipeCategory.MISC
    val ingredients: MutableList<Ingredient> = mutableListOf()

    override fun createRecipe(): ShapelessRecipe = ShapelessRecipe(
        commonInfo(true),
        RecipeBuilder.createCraftingBookInfo(category, group),
        result.template,
        ingredients,
    )
}
