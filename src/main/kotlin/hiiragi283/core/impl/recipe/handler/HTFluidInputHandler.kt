package hiiragi283.core.impl.recipe.handler

import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.ingredient.HTIngredient
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidTank

class HTFluidInputHandler(tank: HTFluidTank) :
    HTInputHandler<HTFluidResourceType>,
    HTFluidTank by tank {
    override fun getMatchingAmount(ingredient: HTIngredient<HTFluidResourceType>): Int {
        val resource: HTFluidResourceType = getResource() ?: return 0
        if (ingredient.testOnlyType(resource)) {
            return ingredient.amount
        }
        return 0
    }

    override fun consume(amount: Int) {
        if (amount > 0) {
            extract(amount, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
    }
}
