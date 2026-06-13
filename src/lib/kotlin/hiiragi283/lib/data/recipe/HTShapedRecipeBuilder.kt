@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.item.ItemInstanceBuilder
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapedRecipe
import net.minecraft.world.item.crafting.ShapedRecipePattern

/**
 * 定形レシピ向けの[HTRecipeBuilder]の実装クラスです。
 *
 * 参照 : [Minecraft - ShapedRecipeBuilder][net.minecraft.data.recipes.ShapedRecipeBuilder]
 * @author Hiiragi Tsubasa
 * @since 26.1.1
 */
class HTShapedRecipeBuilder : HTRecipeBuilder<ShapedRecipe>(HTConstants.SHAPED) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTShapedRecipeBuilder.() -> Unit): HTShapedRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTShapedRecipeBuilder().apply(builderAction)
        }
    }

    /**
     * レシピ本のカテゴリ
     */
    var category: RecipeCategory = RecipeCategory.MISC

    /**
     * レシピ本でのグループ
     */
    var group: String? = null

    @PublishedApi internal val patterns: MutableList<String> = mutableListOf()

    @PublishedApi internal val keys: MutableMap<Char, Ingredient> = mutableMapOf()

    @PublishedApi internal var result: ItemStackTemplate by HTDelegates.onceInitialize()

    operator fun String.unaryPlus() {
        patterns.firstOrNull()?.let {
            if (it.length != this.length) error("Pattern must be the same width on every line")
        }
        patterns += this
    }

    infix fun Char.define(ingredient: Ingredient) {
        check(this !in keys) { "Symbol $this is already defined" }
        check(this != ' ') { "Symbol $this (whitespace) is reserved and cannot be defined" }
        keys[this] = ingredient
    }

    operator fun ItemStackTemplate.unaryPlus() {
        result = this
    }

    inline fun define(key: Char, builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        key define IngredientBuilder().apply(builderAction).build()
    }

    inline fun result(builderAction: ItemInstanceBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        result = ItemInstanceBuilder.buildTemplate(builderAction)
    }

    override fun getPrimalId(): Identifier = result.item().getKeyOrThrow().identifier()

    override fun createRecipe(): ShapedRecipe = ShapedRecipe(
        commonInfo(true),
        bookInfo(category, group),
        ShapedRecipePattern.of(keys, patterns),
        result,
    )
}
