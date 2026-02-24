package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

class HCAnvilCrushingRecipe(ingredient: HTItemIngredient, result: HTItemResult, val extraResult: HTChancedItemResult?) :
    HCSingleItemRecipe<SingleRecipeInput>(ingredient, result) {
    constructor(
        ingredient: HTItemIngredient,
        result: HTItemResult,
        extraResult: Optional<HTChancedItemResult>,
    ) : this(ingredient, result, extraResult.getOrNull())

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.ANVIL_CRUSHING

    override fun getType(): RecipeType<*> = HCRecipeTypes.ANVIL_CRUSHING.get()

    override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())
}
