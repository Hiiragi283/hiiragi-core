package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.data.holder.HTIorHolder
import hiiragi283.core.api.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.core.api.function.identityLeft
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.Ior
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import net.minecraft.resources.ResourceLocation

class HTItemOrFluidRecipeBuilder(prefix: String, private val factory: Factory<out HTSerializableRecipe<*>>) : HTProgressRecipeBuilder(prefix) {
    val ingredient: HTIorHolder<HTItemIngredient, HTFluidIngredient> = HTIorHolder()
    val result: HTIorHolder<HTItemResult, HTFluidResult> = HTIorHolder()

    override fun getPrimalId(): ResourceLocation = result.toIor().map(HTItemResult::getId, HTFluidResult::getId, identityLeft())

    override fun createRecipe(): HTSerializableRecipe<*> = factory.create(
        ingredient.toIor(),
        result.toIor(),
        progressData,
    )

    //    Factory    //

    fun interface Factory<RECIPE : Any> {
        fun create(
            ingredient: Ior<HTItemIngredient, HTFluidIngredient>,
            result: Ior<HTItemResult, HTFluidResult>,
            progressData: HTProgressData,
        ): RECIPE
    }
}
