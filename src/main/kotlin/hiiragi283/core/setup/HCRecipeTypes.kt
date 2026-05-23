package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTDeferredRecipeType
import hiiragi283.core.api.registry.HTDeferredRecipeTypeRegister
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe

object HCRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(HiiragiCoreAPI.MOD_ID)

    //    Basic    //

    @JvmField
    val CHARGING: HTDeferredRecipeType<HCChargingRecipe> = REGISTER.registerType(HTConst.CHARGING)

    @JvmField
    val CRUSHING: HTDeferredRecipeType<HCCrushingRecipe> = REGISTER.registerType(HTConst.CRUSHING)

    @JvmField
    val EXPLODING: HTDeferredRecipeType<HCExplodingRecipe> = REGISTER.registerType(HTConst.EXPLODING)

    //    Tank Interaction    //

    @JvmField
    val EMPTYING: HTDeferredRecipeType<HCTankEmptyingRecipe> = REGISTER.registerType(HTConst.EMPTYING)

    @JvmField
    val FILLING: HTDeferredRecipeType<HCTankFillingRecipe> = REGISTER.registerType(HTConst.FILLING)
}
