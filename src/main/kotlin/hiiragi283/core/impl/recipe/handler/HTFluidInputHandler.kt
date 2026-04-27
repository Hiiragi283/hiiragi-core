package hiiragi283.core.impl.recipe.handler

import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.ingredient.HTIngredient
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidTank

class HTFluidInputHandler(private val tank: HTFluidTank) :
    HTInputHandler<HTFluidResourceType>,
    HTFluidTank by tank {
    override fun consume(ingredient: HTIngredient<HTFluidResourceType>) {
        val resource: HTFluidResourceType = getResource() ?: return
        ingredient.getRequiredAmount(resource, getAmount()).let(::consume)
    }

    override fun consume(amount: Int) {
        if (amount > 0) {
            extract(amount, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
    }

    override fun getCapacity(): Int = tank.getCapacity()

    override fun isEmpty(): Boolean = tank.isEmpty()
}
