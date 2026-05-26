package hiiragi283.core.setup

import hiiragi283.core.api.HCConstants
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import hiiragi283.core.common.recipe.HCChoppingRecipe
import hiiragi283.lib.registry.HTDeferredRecipeType
import hiiragi283.lib.registry.HTDeferredRecipeTypeRegister

data object HCRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(HiiragiCoreAPI.MOD_ID)

    //    In World    //

    @JvmField
    val CHARGING: HTDeferredRecipeType<HCChargingRecipe> = REGISTER.registerType(HCConstants.CHARGING)

    @JvmField
    val CHOPPING: HTDeferredRecipeType<HCChoppingRecipe> = REGISTER.registerType(HCConstants.CHOPPING)

    @JvmField
    val EXPLODING: HTDeferredRecipeType<HCExplodingRecipe> = REGISTER.registerType(HCConstants.EXPLODING)

    //    Tank Interaction    //

    @JvmField
    val EMPTYING: HTDeferredRecipeType<HCTankEmptyingRecipe> = REGISTER.registerType(HCConstants.EMPTYING)

    @JvmField
    val FILLING: HTDeferredRecipeType<HCTankFillingRecipe> = REGISTER.registerType(HCConstants.FILLING)
}
