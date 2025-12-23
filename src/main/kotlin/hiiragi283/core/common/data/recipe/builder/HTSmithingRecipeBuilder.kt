package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.data.recipe.builder.HTIngredientRecipeBuilder
import hiiragi283.core.api.data.recipe.builder.HTStackRecipeBuilder
import hiiragi283.core.api.stack.ImmutableItemStack
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SmithingTransformRecipe
import net.minecraft.world.level.ItemLike

/**
 * [SmithingTransformRecipe]向けの[HTStackRecipeBuilder]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
class HTSmithingRecipeBuilder(stack: ImmutableItemStack) :
    HTStackRecipeBuilder<HTSmithingRecipeBuilder>("smithing", stack),
    HTIngredientRecipeBuilder<HTSmithingRecipeBuilder> {
    companion object {
        /**
         * [SmithingTransformRecipe]のビルダーを作成します。
         */
        @JvmStatic
        fun create(item: ItemLike, count: Int = 1): HTSmithingRecipeBuilder = HTSmithingRecipeBuilder(ImmutableItemStack.of(item, count))
    }

    private val ingredients: MutableList<Ingredient> = mutableListOf()

    override fun addIngredient(ingredient: Ingredient): HTSmithingRecipeBuilder = apply {
        check(ingredients.size <= 2) { "Ingredient has already been initialized!" }
        ingredients.add(ingredient)
    }

    override fun createRecipe(output: ItemStack): SmithingTransformRecipe = SmithingTransformRecipe(
        ingredients[0],
        ingredients[1],
        ingredients.getOrNull(2) ?: Ingredient.of(),
        output,
    )
}
