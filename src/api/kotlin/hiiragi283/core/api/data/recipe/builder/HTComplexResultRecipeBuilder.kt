package hiiragi283.core.api.data.recipe.builder

import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.recipe.result.toComplex
import net.minecraft.resources.ResourceLocation

abstract class HTComplexResultRecipeBuilder<BUILDER : HTComplexResultRecipeBuilder<BUILDER>>(prefix: String) :
    HTRecipeBuilder<BUILDER>(prefix) {
    private var itemResult: HTItemResult? = null
    private var fluidResult: HTFluidResult? = null

    fun setResult(result: HTItemResult?): BUILDER {
        check(this.itemResult == null) { "Item result already initialized!" }
        this.itemResult = result
        return self()
    }

    fun setResult(result: HTFluidResult?): BUILDER {
        check(this.fluidResult == null) { "Fluid result already initialized!" }
        this.fluidResult = result
        return self()
    }

    final override fun getPrimalId(): ResourceLocation = toIorResult().map(HTItemResult::getId, HTFluidResult::getId)

    protected fun toIorResult(): HTComplexResult = (itemResult to fluidResult).toComplex()
}
