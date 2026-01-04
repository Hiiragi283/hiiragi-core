package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.data.recipe.builder.HTStackRecipeBuilder
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTPrefixLike
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapedRecipe
import net.minecraft.world.item.crafting.ShapedRecipePattern
import net.minecraft.world.level.ItemLike
import java.util.stream.Stream

/**
 * [ShapedRecipe]向けの[HTStackRecipeBuilder]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
class HTShapedRecipeBuilder(stack: ItemStack) : HTStackRecipeBuilder<HTShapedRecipeBuilder>("shaped", stack) {
    companion object {
        /**
         * [ShapedRecipe]のビルダーを作成します。
         */
        @JvmStatic
        fun create(item: ItemLike, count: Int = 1): HTShapedRecipeBuilder = HTShapedRecipeBuilder(ItemStack(item, count))
    }

    private val symbols: MutableMap<Char, Ingredient> = mutableMapOf()

    /**
     * 指定した[文字][symbol]に[プレフィックス][prefix]と[素材][material]を指定します。
     */
    fun define(symbol: Char, prefix: HTPrefixLike, material: HTMaterialLike): HTShapedRecipeBuilder =
        define(symbol, prefix.itemTagKey(material))

    /**
     * 指定した[文字][symbol]に[タグ][tagKey]を指定します。
     */
    fun define(symbol: Char, tagKey: TagKey<Item>): HTShapedRecipeBuilder = define(symbol, Ingredient.of(tagKey))

    /**
     * 指定した[文字][symbol]に[アイテム][item]を指定します。
     */
    fun define(symbol: Char, item: ItemLike): HTShapedRecipeBuilder = define(symbol, Ingredient.of(item))

    /**
     * 指定した[文字][symbol]に[材料][values]を指定します。
     */
    fun define(symbol: Char, vararg values: Ingredient.Value): HTShapedRecipeBuilder =
        define(symbol, Ingredient.fromValues(Stream.of(*values)))

    /**
     * 指定した[文字][symbol]に[材料][ingredient]を指定します。
     * @throws IllegalStateException 指定した文字に材料が既に登録されている場合
     * @throws IllegalStateException 指定した文字が空白の場合
     */
    fun define(symbol: Char, ingredient: Ingredient): HTShapedRecipeBuilder = apply {
        check(symbol !in symbols) { "Symbol '$symbol' is already used!" }
        check(symbol != ' ') { "Symbol ' ' is not allowed!" }
        symbols[symbol] = ingredient
    }

    private lateinit var patterns: List<String>

    /**
     * 材料のパターンを指定します。
     */
    fun pattern(vararg pattern: String): HTShapedRecipeBuilder = pattern(pattern.toList())

    /**
     * 材料のパターンを指定します。
     * @throws IllegalStateException 各行のパターンの長さが同じでない場合
     */
    fun pattern(patterns: Iterable<String>): HTShapedRecipeBuilder = apply {
        check(!this::patterns.isInitialized) { "Patterns has already been initialized!" }
        check(patterns.map(String::length).toSet().size == 1) { "Each pattern must be the same length!" }
        this.patterns = patterns.toList()
    }

    /**
     * 2x2のパターンを指定します。
     */
    fun storage4(): HTShapedRecipeBuilder = pattern("AA", "AA")

    /**
     * 3x3のパターンを指定します。
     */
    fun storage9(): HTShapedRecipeBuilder = pattern("AAA", "AAA", "AAA")

    /**
     * 中央が空の3x3のパターンを指定します。
     */
    fun hollow(): HTShapedRecipeBuilder = pattern("AAA", "A A", "AAA")

    /**
     * 中央の材料を，一種類の4つの材料で取り囲むパターンを指定します。
     */
    fun hollow4(): HTShapedRecipeBuilder = pattern(" A ", "ABA", " A ")

    /**
     * 中央の材料を，一種類の8つの材料で取り囲むパターンを指定します。
     */
    fun hollow8(): HTShapedRecipeBuilder = pattern("AAA", "ABA", "AAA")

    /**
     * 中央の材料を，二種類の2つずつの材料で取り囲むパターンを指定します。
     */
    fun cross4(): HTShapedRecipeBuilder = pattern(" A ", "BCB", " A ")

    /**
     * 中央の材料を，二種類の4つずつの材料で取り囲むパターンを指定します。
     */
    fun cross8(): HTShapedRecipeBuilder = pattern("ABA", "BCB", "ABA")

    fun crossLayered(): HTShapedRecipeBuilder = pattern("ABA", "CDC", "ABA")

    /**
     * 二種類の材料を交互に配置する2x2のパターンを指定します。
     */
    fun mosaic4(): HTShapedRecipeBuilder = pattern("AB", "BA")

    /**
     * 二種類の材料を交互に配置する3x3のパターンを指定します。
     */
    fun mosaic9(): HTShapedRecipeBuilder = pattern("ABA", "BAB", "ABA")

    //    RecipeBuilder    //

    private var group: String? = null
    private var category: CraftingBookCategory = CraftingBookCategory.MISC

    /**
     * レシピのグループを[getPrimalId]から指定します。
     */
    fun setGroup(): HTShapedRecipeBuilder = setGroup(getPrimalId().toDebugFileName())

    /**
     * レシピのグループを指定します。
     */
    fun setGroup(group: String?): HTShapedRecipeBuilder = apply {
        this.group = group
    }

    /**
     * レシピのカテゴリを指定します。
     */
    fun setCategory(category: CraftingBookCategory): HTShapedRecipeBuilder = apply {
        this.category = category
    }

    override fun createRecipe(output: ItemStack): ShapedRecipe = ShapedRecipe(
        group ?: "",
        category,
        ShapedRecipePattern.of(symbols, patterns),
        output,
        true,
    )
}
