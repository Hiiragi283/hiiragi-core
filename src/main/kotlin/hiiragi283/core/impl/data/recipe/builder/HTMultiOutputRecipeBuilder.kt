package hiiragi283.core.impl.data.recipe.builder

import hiiragi283.core.api.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.recipe.result.HTListItemResult
import net.minecraft.resources.ResourceLocation

abstract class HTMultiOutputRecipeBuilder(prefix: String) : HTProgressRecipeBuilder(prefix) {
    val results: MutableList<HTItemResult> = mutableListOf()

    protected fun createList(): HTListItemResult = HTListItemResult(results)

    final override fun getPrimalId(): ResourceLocation = results.first().getId()
}
