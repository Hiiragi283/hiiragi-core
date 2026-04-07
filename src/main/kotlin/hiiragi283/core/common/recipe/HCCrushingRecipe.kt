package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.impl.recipe.HTBasicItemToMultiOutputRecipe
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HCCrushingRecipe(ingredient: HTItemIngredient, results: List<HTItemResult>, time: Int) :
    HTBasicItemToMultiOutputRecipe(ingredient, results, time) {
    companion object {
        @JvmField
        val OUTPUT_RANGE: IntRange = 1..4
    }

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.CRUSHING

    override fun getType(): RecipeType<*> = HCRecipeTypes.CRUSHING.get()
}
