package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe

class HTItemToResultRecipeBuilder<RES : HTIdLike>(prefix: String, private val factory: Factory<RES, out HTSerializableRecipe<*>>) : HTProgressRecipeBuilder(prefix) {
    var ingredient: HTItemIngredient by HTDelegates.onceInitialize()
    var result: RES by HTDelegates.onceInitialize()

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): Recipe<*> = factory.create(ingredient, result, progressData)

    //    Factory    //

    fun interface Factory<RES : Any, RECIPE : Any> {
        fun create(ingredient: HTItemIngredient, result: RES, progressData: HTProgressData): RECIPE
    }
}
