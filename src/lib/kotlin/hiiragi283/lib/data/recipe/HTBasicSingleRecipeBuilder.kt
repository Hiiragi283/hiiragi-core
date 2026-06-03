package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.util.HTDelegates
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe

class HTBasicSingleRecipeBuilder<I : Any, O : HTIdLike, out RECIPE : Recipe<*>>(prefix: String, private val factory: Factory<I, O, RECIPE>) : HTProgressRecipeBuilder<RECIPE>(prefix) {
    var ingredient: I by HTDelegates.onceInitialize()
    var result: O by HTDelegates.onceInitialize()

    override fun getPrimalId(): Identifier = result.getId()

    override fun createRecipe(): RECIPE = factory.create(ingredient, result, progressData)

    //    Factory    //

    fun interface Factory<I : Any, O : Any, out RECIPE> {
        fun create(ingredient: I, result: O, progressData: HTProgressData): RECIPE
    }
}
