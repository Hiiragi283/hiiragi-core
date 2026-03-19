package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.base.HTItemToItemRecipe
import hiiragi283.core.impl.recipe.HTBasicItemToItemRecipe
import hiiragi283.core.setup.HCRecipeBookCategories
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.crafting.RecipeBookCategory
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.common.crafting.SizedIngredient

class HCChargingRecipe(ingredient: SizedIngredient, result: HTItemResult, time: Int) :
    HTBasicItemToItemRecipe(ingredient, result, time) {
    override fun getSerializer(): RecipeSerializer<HCChargingRecipe> = HCRecipeSerializers.CHARGING

    override fun getType(): RecipeType<HTItemToItemRecipe.Serializable> = HCRecipeTypes.CHARGING.get()

    override fun recipeBookCategory(): RecipeBookCategory = HCRecipeBookCategories.CHARGING
}
