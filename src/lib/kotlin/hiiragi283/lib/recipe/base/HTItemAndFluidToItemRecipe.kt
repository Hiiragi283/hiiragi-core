package hiiragi283.lib.recipe.base

import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import net.minecraft.world.item.ItemStack

/**
 * 1種類のアイテムと液体から，1種類のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTItemAndFluidToItemRecipe :
    HTRecipePredicates.ItemAndFluid,
    HTRecipeFactories.ItemAndFluid<ItemStack>,
    HTProgressRecipe<HTItemAndFluidRecipeInput>
