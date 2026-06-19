package hiiragi283.lib.recipe.base

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * 1種類のアイテムから1種類のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTItemToItemRecipe :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<ItemStack>,
    HTProgressRecipe<SingleRecipeInput>
