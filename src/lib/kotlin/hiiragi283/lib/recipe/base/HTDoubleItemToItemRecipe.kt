package hiiragi283.lib.recipe.base

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 2種類のアイテムから1種類のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTDoubleItemToItemRecipe :
    HTRecipePredicates.DoubleItem,
    HTRecipeFactories.DoubleItem<ItemStack>,
    HTProgressRecipe<RecipeInput>
