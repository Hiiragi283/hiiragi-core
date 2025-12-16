package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.common.recipe.HTDryingRecipe
import hiiragi283.core.common.recipe.HTFrostingRecipe
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister

object HCRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val DRYING: HTDeferredRecipeType<HTRecipeInput, HTDryingRecipe> = REGISTER.registerType(HTConst.DRYING)

    @JvmField
    val FROSTING: HTDeferredRecipeType<HTRecipeInput, HTFrostingRecipe> = REGISTER.registerType(HTConst.FROSTING)
}
