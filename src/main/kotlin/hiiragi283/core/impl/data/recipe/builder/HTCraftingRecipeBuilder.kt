package hiiragi283.core.impl.data.recipe.builder

import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.world.item.crafting.CraftingRecipe

abstract class HTCraftingRecipeBuilder(prefix: String) : HTAbstractSingleItemRecipeBuilder(prefix) {
    var group: String? = null
    var category: RecipeCategory = RecipeCategory.MISC

    fun bookInfo(): CraftingRecipe.CraftingBookInfo = RecipeBuilder.createCraftingBookInfo(category, group)
}
