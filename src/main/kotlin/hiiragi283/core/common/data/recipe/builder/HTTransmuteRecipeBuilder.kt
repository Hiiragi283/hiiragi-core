package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.impl.data.recipe.builder.HTCraftingRecipeBuilder
import net.minecraft.advancements.criterion.MinMaxBounds
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.TransmuteRecipe

/**
 * @see net.minecraft.data.recipes.TransmuteRecipeBuilder
 */
class HTTransmuteRecipeBuilder : HTCraftingRecipeBuilder("transmute") {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTTransmuteRecipeBuilder.() -> Unit) {
            HTTransmuteRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: Ingredient
    lateinit var material: Ingredient
    var materialCount: MinMaxBounds.Ints = TransmuteRecipe.DEFAULT_MATERIAL_COUNT
    var addMaterialCoundToCount: Boolean = false

    override fun createRecipe(): TransmuteRecipe = TransmuteRecipe(
        commonInfo(true),
        bookInfo(),
        ingredient,
        material,
        materialCount,
        result.template,
        addMaterialCoundToCount,
    )
}
