package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.base.factory.HTItemAndFluidRecipeFactory
import hiiragi283.core.api.recipe.base.predicate.HTSingleRecipePredicate
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * 液体入りの容器から，空の容器と液体を取り出すレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.1
 */
interface HTTankEmptyingRecipe :
    HTSingleRecipePredicate.SingleItem,
    HTItemAndFluidRecipeFactory<SingleRecipeInput>
