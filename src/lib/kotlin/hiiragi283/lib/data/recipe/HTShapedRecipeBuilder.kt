@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.HTConstants
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
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
class HTShapedRecipeBuilder : HTCraftingRecipeBuilder<ShapedRecipe>(HTConstants.SHAPED) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTShapedRecipeBuilder.() -> Unit): HTShapedRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTShapedRecipeBuilder().apply(builderAction)
        }
    }

    @PublishedApi internal val patterns: MutableList<String> = mutableListOf()

    @PublishedApi internal val keys: MutableMap<Char, Ingredient> = mutableMapOf()

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

    inline fun define(key: Char, builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        key define IngredientBuilder().apply(builderAction).build()
    }

    override fun createRecipe(): ShapedRecipe = ShapedRecipe(
        commonInfo(true),
        bookInfo(),
        ShapedRecipePattern.of(keys, patterns),
        result,
    )
}
