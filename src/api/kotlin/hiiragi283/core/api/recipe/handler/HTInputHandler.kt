package hiiragi283.core.api.recipe.handler

import hiiragi283.core.api.recipe.ingredient.HTIngredient
import hiiragi283.core.api.storage.resource.HTResourceType
import java.util.Optional

/**
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see mekanism.api.recipes.inputs.IInputHandler
 */
interface HTInputHandler<RESOURCE : HTResourceType<*>> {
    fun getMatchingAmount(ingredient: HTIngredient<*, RESOURCE>): Int

    fun consume(ingredient: HTIngredient<*, RESOURCE>?) {
        ingredient?.let(::getMatchingAmount)?.let(::consume)
    }

    fun consume(ingredient: Optional<out HTIngredient<*, RESOURCE>>) {
        ingredient.map(::consume)
    }

    fun consume(amount: Int)
}
