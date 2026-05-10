package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import net.minecraft.resources.ResourceLocation

class HTItemAndFluidToItemRecipeBuilder(prefix: String, private val factory: Factory<out HTSerializableRecipe<*>>) : HTProgressRecipeBuilder(prefix) {
    lateinit var itemIngredient: HTItemIngredient
    lateinit var fluidIngredient: HTFluidIngredient
    lateinit var result: HTItemResult

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTSerializableRecipe<*> = factory.create(
        itemIngredient,
        fluidIngredient,
        result,
        progressData,
    )

    //    Factory    //

    fun interface Factory<RECIPE : Any> {
        fun create(
            itemIngredient: HTItemIngredient,
            fluidIngredient: HTFluidIngredient,
            result: HTItemResult,
            progressData: HTProgressData,
        ): RECIPE
    }
}
