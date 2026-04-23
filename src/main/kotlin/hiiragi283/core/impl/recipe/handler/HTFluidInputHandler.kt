package hiiragi283.core.impl.recipe.handler

import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.getFluidStack

class HTFluidInputHandler(tank: HTFluidTank) :
    HTInputHandler<HTFluidIngredient>,
    HTFluidTank by tank {
    override fun getMatchingAmount(ingredient: HTFluidIngredient): Int = ingredient.getRequiredAmount(this.getFluidStack())

    override fun consume(amount: Int) {
        if (amount > 0) {
            extract(amount, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
    }
}
