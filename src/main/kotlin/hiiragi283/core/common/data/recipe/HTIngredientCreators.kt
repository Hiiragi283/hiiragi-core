package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.common.recipe.ingredient.HTBluePrintIngredient

fun HTIngredientCreator.blueprint(number: Int): HTItemIngredient = this.create(HTBluePrintIngredient(number).toVanilla(), 0)
