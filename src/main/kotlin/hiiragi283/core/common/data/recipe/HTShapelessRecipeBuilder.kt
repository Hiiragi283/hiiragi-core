@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.HTRecipeBuilder
import hiiragi283.core.api.data.recipe.IngredientBuilder
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapelessRecipe

/**
 * 不定形レシピ向けの[HTRecipeBuilder]の実装クラスです。
 *
 * 参照 : [Minecraft - ShapelessRecipeBuilder][net.minecraft.data.recipes.ShapelessRecipeBuilder]
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
class HTShapelessRecipeBuilder : HTCraftingRecipeBuilder<ShapelessRecipe>(HTConst.SHAPELESS) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTShapelessRecipeBuilder.() -> Unit): HTShapelessRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTShapelessRecipeBuilder().apply(builderAction)
        }
    }

    @PublishedApi internal val ingredients: MutableList<Ingredient> = mutableListOf()

    operator fun Ingredient.unaryPlus() {
        ingredients += this
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ingredients += IngredientBuilder().apply(builderAction).build()
    }

    override fun createRecipe(group: String, category: CraftingBookCategory, result: ItemStack): ShapelessRecipe = ShapelessRecipe(
        group,
        category,
        result,
        NonNullList.copyOf(ingredients),
    )
}
