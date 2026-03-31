package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.function.partially1
import hiiragi283.core.impl.data.recipe.builder.HTCraftingRecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapedRecipe
import net.minecraft.world.item.crafting.ShapedRecipePattern

class HTShapedRecipeBuilder : HTCraftingRecipeBuilder(HTConst.SHAPED) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTShapedRecipeBuilder.() -> Unit) {
            HTShapedRecipeBuilder().apply(builderAction).save(output)
        }
    }

    private lateinit var patterns: List<String>
    private val symbols: MutableMap<Char, Ingredient> = mutableMapOf()

    fun define(symbol: Char): (Ingredient) -> Unit {
        check(symbol !in symbols) { "Symbol '$symbol' is already used!" }
        check(symbol != ' ') { "Symbol ' ' is not allowed!" }
        return symbols::set.partially1(symbol)
    }

    override fun createRecipe(): ShapedRecipe = ShapedRecipe(
        commonInfo(true),
        bookInfo(),
        ShapedRecipePattern.of(symbols, patterns),
        result.template,
    )

    //    Patterns    //

    /**
     * 材料のパターンを指定します。
     * @throws IllegalStateException 各行のパターンの長さが同じでない場合
     */
    fun pattern(patterns: Iterable<String>) {
        check(!this::patterns.isInitialized) { "Patterns has already been initialized!" }
        check(patterns.map(String::length).toSet().size == 1) { "Each pattern must be the same length!" }
        this.patterns = patterns.toList()
    }

    /**
     * 材料のパターンを指定します。
     */
    fun pattern(vararg pattern: String) {
        pattern(pattern.toList())
    }

    /**
     * 2x2のパターンを指定します。
     */
    fun storage4() {
        pattern("AA", "AA")
    }

    /**
     * 3x3のパターンを指定します。
     */
    fun storage9() {
        pattern("AAA", "AAA", "AAA")
    }

    /**
     * 中央が空の3x3のパターンを指定します。
     */
    fun hollow() {
        pattern("AAA", "A A", "AAA")
    }

    /**
     * 中央の材料を，一種類の4つの材料で取り囲むパターンを指定します。
     */
    fun hollow4() {
        pattern(" A ", "ABA", " A ")
    }

    /**
     * 中央の材料を，一種類の8つの材料で取り囲むパターンを指定します。
     */
    fun hollow8() {
        pattern("AAA", "ABA", "AAA")
    }

    /**
     * 中央の材料を，二種類の2つずつの材料で取り囲むパターンを指定します。
     */
    fun cross4() {
        pattern(" A ", "BCB", " A ")
    }

    /**
     * 中央の材料を，二種類の4つずつの材料で取り囲むパターンを指定します。
     */
    fun cross8() {
        pattern("ABA", "BCB", "ABA")
    }

    fun crossLayered() {
        pattern("ABA", "CDC", "ABA")
    }

    /**
     * 二種類の材料を交互に配置する2x2のパターンを指定します。
     */
    fun mosaic4() {
        pattern("AB", "BA")
    }

    /**
     * 二種類の材料を交互に配置する3x3のパターンを指定します。
     */
    fun mosaic9() {
        pattern("ABA", "BAB", "ABA")
    }
}
