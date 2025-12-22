package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import org.apache.commons.lang3.math.Fraction

class HTExplodingRecipe(
    ingredient: HTItemIngredient,
    result: HTItemResult,
    time: Int,
    exp: Fraction,
) : HTSingleItemRecipe(ingredient, result, time, exp) {
    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.EXPLODING

    override fun getType(): RecipeType<*> = HCRecipeTypes.EXPLODING.get()
}
