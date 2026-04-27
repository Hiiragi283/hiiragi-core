package hiiragi283.core.impl.recipe

import hiiragi283.core.api.recipe.base.HTMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.util.HTShapelessRecipeHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

abstract class HTBasicMultiOutputRecipe<INPUT : RecipeInput>(val results: List<HTItemResult>, final override val time: Int) :
    HTMultiOutputRecipe.Serializable<INPUT>,
    HTProgressRecipe.Ticking<INPUT> {
    final override fun assembleItems(input: INPUT, preview: Boolean): List<ItemStack> =
        results.map { it.getOrEmpty(preview) }.let(HTShapelessRecipeHelper::mergeStacks)
}
