@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.data.recipe.HTRecipeBuilder
import hiiragi283.core.api.item.ItemInstanceBuilder
import hiiragi283.core.api.registry.getKeyOrThrow
import hiiragi283.core.api.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingRecipe

/**
 * クラフトレシピ向けの[HTRecipeBuilder]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
abstract class HTCraftingRecipeBuilder<out RECIPE : CraftingRecipe>(prefix: String) : HTRecipeBuilder<RECIPE>(prefix) {
    /**
     * レシピ本のカテゴリ
     */
    var category: RecipeCategory = RecipeCategory.MISC

    /**
     * レシピ本でのグループ
     */
    var group: String? = null

    @PublishedApi internal var result: ItemStack by HTDelegates.onceInitialize()

    operator fun ItemStack.unaryPlus() {
        result = this
    }

    inline fun result(builderAction: ItemInstanceBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        result = ItemInstanceBuilder.buildStack(builderAction)
    }

    final override fun getPrimalId(): ResourceLocation = result.itemHolder.getKeyOrThrow().location()

    final override fun createRecipe(): RECIPE = createRecipe(group ?: "", RecipeBuilder.determineBookCategory(category), result)

    protected abstract fun createRecipe(group: String, category: CraftingBookCategory, result: ItemStack): RECIPE
}
