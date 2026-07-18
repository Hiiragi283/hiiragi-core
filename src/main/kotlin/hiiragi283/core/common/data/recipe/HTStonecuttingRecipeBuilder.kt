@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.data.recipe.HTRecipeBuilder
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.item.ItemInstanceBuilder
import hiiragi283.core.api.registry.getKeyOrThrow
import hiiragi283.core.api.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.StonecutterRecipe

/**
 * 石切台レシピ向けの[HTRecipeBuilder]の実装クラスです。
 *
 * 参照 : [Minecraft - SingleItemRecipeBuilder][net.minecraft.data.recipes.SingleItemRecipeBuilder]
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
class HTStonecuttingRecipeBuilder : HTRecipeBuilder<StonecutterRecipe>("stonecutting") {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTStonecuttingRecipeBuilder.() -> Unit): HTStonecuttingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTStonecuttingRecipeBuilder().apply(builderAction)
        }
    }

    /**
     * レシピ本でのグループ
     */
    var group: String? = null

    @PublishedApi internal var ingredient: Ingredient by HTDelegates.onceInitialize()

    @PublishedApi internal var result: ItemStack by HTDelegates.onceInitialize()

    operator fun Ingredient.unaryPlus() {
        ingredient = this
    }

    operator fun ItemStack.unaryPlus() {
        result = this
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ingredient = IngredientBuilder().apply(builderAction).build()
    }

    inline fun result(builderAction: ItemInstanceBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        result = ItemInstanceBuilder.buildStack(builderAction)
    }

    override fun getPrimalId(): ResourceLocation = result.itemHolder.getKeyOrThrow().location()

    override fun createRecipe(): StonecutterRecipe = StonecutterRecipe(group ?: "", ingredient, result)
}
