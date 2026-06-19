package hiiragi283.lib.recipe.base

import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 1種類のアイテムから1種類の液体を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTItemToFluidRecipe :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<FluidStack>,
    HTProgressRecipe<SingleRecipeInput>
