package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.impl.recipe.HTBasicItemToChancedRecipe
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import java.util.Optional

class HCCrushingRecipe(
    ingredient: HTItemIngredient,
    result: HTItemResult,
    extraResult: Optional<HTItemResult>,
    time: Int,
) : HTBasicItemToChancedRecipe(ingredient, result, extraResult, time) {
    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.CRUSHING

    override fun getType(): RecipeType<*> = HCRecipeTypes.CRUSHING.get()
}
