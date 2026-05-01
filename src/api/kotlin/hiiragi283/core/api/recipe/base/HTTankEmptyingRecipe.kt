package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.util.Ior
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 液体入りの容器から，空の容器と液体を取り出すレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.1
 */
interface HTTankEmptyingRecipe :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<Ior<ItemStack, FluidStack>>
