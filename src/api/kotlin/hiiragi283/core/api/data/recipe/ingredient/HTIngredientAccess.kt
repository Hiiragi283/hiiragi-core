package hiiragi283.core.api.data.recipe.ingredient

import hiiragi283.core.api.HiiragiCoreAPI

interface HTIngredientAccess {
    companion object {
        @JvmField
        val INSTANCE: HTIngredientAccess = HiiragiCoreAPI.getService()
    }

    fun fluidCreator(): HTFluidIngredientCreator

    fun itemCreator(): HTItemIngredientCreator
}
