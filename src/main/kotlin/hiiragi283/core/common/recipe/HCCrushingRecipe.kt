package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.base.HTItemToChancedRecipe
import hiiragi283.core.impl.recipe.HTBasicItemToChancedRecipe
import hiiragi283.core.setup.HCRecipeBookCategories
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.crafting.RecipeBookCategory
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.common.crafting.SizedIngredient
import java.util.Optional

class HCCrushingRecipe(
    ingredient: SizedIngredient,
    result: HTItemResult,
    extraResult: Optional<HTItemResult>,
    time: Int,
) : HTBasicItemToChancedRecipe(ingredient, result, extraResult, time) {
    override fun getSerializer(): RecipeSerializer<HCCrushingRecipe> = HCRecipeSerializers.CRUSHING

    override fun getType(): RecipeType<HTItemToChancedRecipe.Serializable> = HCRecipeTypes.CRUSHING.get()

    override fun recipeBookCategory(): RecipeBookCategory = HCRecipeBookCategories.CRUSHING
}
