package hiiragi283.core.common.data.recipe.ingredient

import hiiragi283.core.api.data.recipe.ingredient.HTFluidIngredientCreator
import hiiragi283.core.api.data.recipe.ingredient.HTIngredientAccess
import hiiragi283.core.api.data.recipe.ingredient.HTItemIngredientCreator

/**
 * @suppress
 */
class HTIngredientAccessImpl : HTIngredientAccess {
    override fun fluidCreator(): HTFluidIngredientCreator = HTFluidIngredientCreatorImpl

    override fun itemCreator(): HTItemIngredientCreator = HTItemIngredientCreatorImpl
}
