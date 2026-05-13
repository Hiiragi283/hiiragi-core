package hiiragi283.core.impl.data.recipe.builder

import hiiragi283.core.api.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import net.minecraft.resources.ResourceLocation

abstract class HTMultiOutputRecipeBuilder(prefix: String) : HTProgressRecipeBuilder(prefix) {
    val results: MutableList<HTChancedItemResult> = mutableListOf()

    protected fun createList(): List<HTChancedItemResult> = results

    final override fun getPrimalId(): ResourceLocation = results.first().getId()
}
