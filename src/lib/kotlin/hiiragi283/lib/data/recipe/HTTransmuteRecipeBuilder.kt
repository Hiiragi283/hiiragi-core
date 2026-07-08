@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.advancements.criterion.MinMaxBounds
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.TransmuteRecipe

/**
 * 変換レシピ向けの[HTRecipeBuilder]の実装クラスです。
 *
 * 参照 : [Minecraft - TransmuteRecipeBuilder][net.minecraft.data.recipes.TransmuteRecipeBuilder]
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
class HTTransmuteRecipeBuilder : HTCraftingRecipeBuilder<TransmuteRecipe>(HTConstants.TRANSMUTE) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTTransmuteRecipeBuilder.() -> Unit): HTTransmuteRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTTransmuteRecipeBuilder().apply(builderAction)
        }
    }

    @PublishedApi internal var input: Ingredient by HTDelegates.onceInitialize()

    @PublishedApi internal var material: Ingredient by HTDelegates.onceInitialize()

    var materialCount: MinMaxBounds.Ints = TransmuteRecipe.DEFAULT_MATERIAL_COUNT
    var addMaterialCountToOutput: Boolean = false

    inline fun input(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        input = IngredientBuilder().apply(builderAction).build()
    }

    inline fun material(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        material = IngredientBuilder().apply(builderAction).build()
    }

    override fun createRecipe(): TransmuteRecipe = TransmuteRecipe(
        commonInfo(true),
        bookInfo(),
        input,
        material,
        materialCount,
        result,
        addMaterialCountToOutput,
    )
}
