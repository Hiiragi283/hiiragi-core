package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.HTRecipe
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

interface HTTankEmptyingRecipe :
    HTRecipe<SingleRecipeInput>,
    HTFluidRecipe<SingleRecipeInput> {
    fun testContainer(stack: ItemStack): Boolean

    //    HTRecipe    //

    override fun test(input: SingleRecipeInput): Boolean = testContainer(input.item())

    //    Serializable    //

    interface Serializable :
        HTTankEmptyingRecipe,
        HTSerializableRecipe<SingleRecipeInput>
}
