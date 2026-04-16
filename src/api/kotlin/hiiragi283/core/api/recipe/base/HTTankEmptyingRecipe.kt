package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.HTRecipe
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * 液体入りの容器から，空の容器と液体を取り出すレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.1
 */
interface HTTankEmptyingRecipe :
    HTRecipe<SingleRecipeInput>,
    HTFluidRecipe<SingleRecipeInput> {
    fun testContainer(stack: ItemStack): Boolean

    //    HTRecipe    //

    override fun test(input: SingleRecipeInput): Boolean = testContainer(input.item())

    //    Serializable    //

    /**
     * シリアライズ可能な[HTTankEmptyingRecipe]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.15.1
     */
    interface Serializable :
        HTTankEmptyingRecipe,
        HTSerializableRecipe<SingleRecipeInput>
}
