package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.impl.recipe.HTBasicDoubleMultiOutputRecipe
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import java.util.Optional

class HCForgingRecipe(
    base: HTItemIngredient,
    addition: Optional<HTItemIngredient>,
    results: List<HTItemResult>,
    time: Int,
) : HTBasicDoubleMultiOutputRecipe(base, addition, results, time) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HCForgingRecipe> = codec(1..9, ::HCForgingRecipe)
    }

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.FORGING

    override fun getType(): RecipeType<*> = HCRecipeTypes.FORGING.get()
}
