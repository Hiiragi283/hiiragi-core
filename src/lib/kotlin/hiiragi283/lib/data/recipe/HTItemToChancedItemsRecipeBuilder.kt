package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTChancedItemResult
import hiiragi283.lib.util.HTDelegates
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe

class HTItemToChancedItemsRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String, private val factory: Factory<RECIPE>) : HTProgressRecipeBuilder<RECIPE>(prefix) {
    var ingredient: HTItemIngredient by HTDelegates.onceInitialize()
    val results = HTChancedItemResultHolder()

    override fun getPrimalId(): Identifier = results.getId()

    override fun createRecipe(): RECIPE = factory.create(ingredient, results.results, progressData)

    //    Factory    //

    fun interface Factory<out RECIPE> {
        fun create(ingredient: HTItemIngredient, results: List<HTChancedItemResult>, progressData: HTProgressData): RECIPE
    }
}
