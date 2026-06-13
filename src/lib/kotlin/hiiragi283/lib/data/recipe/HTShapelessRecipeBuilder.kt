@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.item.ItemInstanceBuilder
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.NonNullList
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapelessRecipe

/**
 * 不定形レシピ向けの[HTRecipeBuilder]の実装クラスです。
 *
 * 参照 : [Minecraft - ShapelessRecipeBuilder][net.minecraft.data.recipes.ShapelessRecipeBuilder]
 * @author Hiiragi Tsubasa
 * @since 26.1.1
 */
class HTShapelessRecipeBuilder : HTRecipeBuilder<ShapelessRecipe>(HTConstants.SHAPELESS) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTShapelessRecipeBuilder.() -> Unit): HTShapelessRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTShapelessRecipeBuilder().apply(builderAction)
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

    @PublishedApi internal var result: ItemStackTemplate by HTDelegates.onceInitialize()

    @PublishedApi internal val ingredients: MutableList<Ingredient> = mutableListOf()

    operator fun Ingredient.unaryPlus() {
        ingredients += this
    }

    operator fun ItemStackTemplate.unaryPlus() {
        result = this
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ingredients += IngredientBuilder().apply(builderAction).build()
    }

    inline fun result(builderAction: ItemInstanceBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        result = ItemInstanceBuilder.buildTemplate(builderAction)
    }

    override fun getPrimalId(): Identifier = result.item().getKeyOrThrow().identifier()

    override fun createRecipe(): ShapelessRecipe = ShapelessRecipe(
        commonInfo(true),
        bookInfo(category, group),
        result,
        NonNullList.copyOf(ingredients),
    )
}
