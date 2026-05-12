package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.impl.data.recipe.builder.HTStackRecipeBuilder
import net.minecraft.core.NonNullList
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapelessRecipe

class HTShapelessRecipeBuilder : HTStackRecipeBuilder(HTConst.SHAPELESS) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTShapelessRecipeBuilder.() -> Unit) {
            HTShapelessRecipeBuilder().apply(builderAction).save(output)
        }
    }

    var group: String? = null
    var category: CraftingBookCategory = CraftingBookCategory.MISC
    var ingredients: MutableList<Ingredient> = mutableListOf()

    override fun createRecipe(): ShapelessRecipe = ShapelessRecipe(
        group ?: "",
        category,
        resultStack,
        NonNullList.copyOf(ingredients),
    )
}
