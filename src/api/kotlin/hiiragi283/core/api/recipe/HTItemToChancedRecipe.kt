package hiiragi283.core.api.recipe

import net.minecraft.world.item.crafting.SingleRecipeInput

interface HTItemToChancedRecipe : HTChancedRecipe<SingleRecipeInput> {
    fun getRequiredAmount(input: SingleRecipeInput): Int

    //    Serializable    //

    interface Serializable :
        HTItemToChancedRecipe,
        HTChancedRecipe.Serializable<SingleRecipeInput>
}
