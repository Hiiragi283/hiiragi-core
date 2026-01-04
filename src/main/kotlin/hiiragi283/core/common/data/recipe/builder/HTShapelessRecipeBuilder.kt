package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTIngredientRecipeBuilder
import hiiragi283.core.api.data.recipe.builder.HTStackRecipeBuilder
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTPrefixLike
import net.minecraft.core.NonNullList
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapelessRecipe
import net.minecraft.world.level.ItemLike

/**
 * [ShapelessRecipe]向けの[HTStackRecipeBuilder]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
class HTShapelessRecipeBuilder(stack: ItemStack) :
    HTStackRecipeBuilder<HTShapelessRecipeBuilder>(HTConst.SHAPELESS, stack),
    HTIngredientRecipeBuilder<HTShapelessRecipeBuilder> {
    companion object {
        /**
         * [ShapelessRecipe]のビルダーを作成します。
         */
        @JvmStatic
        fun create(item: ItemLike, count: Int = 1): HTShapelessRecipeBuilder = HTShapelessRecipeBuilder(ItemStack(item, count))
    }

    private val ingredients: NonNullList<Ingredient> = NonNullList.create()

    override fun addIngredient(ingredient: Ingredient): HTShapelessRecipeBuilder = apply {
        ingredients.add(ingredient)
    }

    fun addIngredients(prefix: HTPrefixLike, key: HTMaterialLike, count: Int): HTShapelessRecipeBuilder =
        addIngredients(prefix.itemTagKey(key), count)

    fun addIngredients(tagKey: TagKey<Item>, count: Int): HTShapelessRecipeBuilder = addIngredients(Ingredient.of(tagKey), count)

    fun addIngredients(vararg items: ItemLike, count: Int): HTShapelessRecipeBuilder = addIngredients(Ingredient.of(*items), count)

    fun addIngredients(ingredient: Ingredient, count: Int): HTShapelessRecipeBuilder = apply {
        repeat(count) {
            addIngredient(ingredient)
        }
    }

    //    RecipeBuilder    //

    private var group: String? = null
    private var category: CraftingBookCategory = CraftingBookCategory.MISC

    /**
     * レシピのグループを指定します。
     */
    fun setGroup(group: String?): HTShapelessRecipeBuilder = apply {
        this.group = group
    }

    /**
     * レシピのカテゴリを指定します。
     */
    fun setCategory(category: CraftingBookCategory): HTShapelessRecipeBuilder = apply {
        this.category = category
    }

    override fun createRecipe(output: ItemStack): ShapelessRecipe = ShapelessRecipe(
        group ?: "",
        category,
        output,
        ingredients,
    )
}
