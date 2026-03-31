package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.setup.HCRecipeBookCategories
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeBookCategory
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HCChargingRecipe(val ingredient: Ingredient, val result: HTItemResult, val energy: Int) :
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        const val DEFAULT_ENERGY = 1_024_000
    }

    override fun assemble(input: SingleRecipeInput): ItemStack = result.create()

    override fun getSerializer(): RecipeSerializer<HCChargingRecipe> = HCRecipeSerializers.CHARGING

    override fun getType(): RecipeType<HCChargingRecipe> = HCRecipeTypes.CHARGING.get()

    override fun recipeBookCategory(): RecipeBookCategory = HCRecipeBookCategories.CHARGING

    override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())
}
