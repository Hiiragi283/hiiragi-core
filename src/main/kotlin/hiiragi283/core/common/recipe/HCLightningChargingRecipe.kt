package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HCLightningChargingRecipe(ingredient: HTItemIngredient, result: HTItemResult) :
    HCSingleItemRecipe<SingleRecipeInput>(ingredient, result) {
    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.CHARGING

    override fun getType(): RecipeType<*> = HCRecipeTypes.CHARGING.get()

    override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())
}
