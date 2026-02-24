package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.base.HTBasicItemToChancedRecipe
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import java.util.Optional

class HTCrushingRecipe(
    ingredient: HTItemIngredient,
    result: HTItemResult,
    extraResult: Optional<HTChancedItemResult>,
    time: Int,
) : HTBasicItemToChancedRecipe(ingredient, result, extraResult, time) {
    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.CRUSHING

    override fun getType(): RecipeType<*> = HCRecipeTypes.CRUSHING.get()
}
