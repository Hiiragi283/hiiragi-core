package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.recipe.base.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.base.HTTankFillingRecipe
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister

object HCRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(HiiragiCoreAPI.MOD_ID)

    //    Basic    //

    @JvmField
    val CHARGING: HTDeferredRecipeType<HCChargingRecipe> = REGISTER.registerType(HTConst.CHARGING)

    @JvmField
    val CRUSHING: HTDeferredRecipeType<HTSingleMultiOutputRecipe.Serializable> = REGISTER.registerType(HTConst.CRUSHING)

    @JvmField
    val EXPLODING: HTDeferredRecipeType<HCExplodingRecipe> = REGISTER.registerType(HTConst.EXPLODING)

    @JvmField
    val FORGING: HTDeferredRecipeType<HTDoubleMultiOutputRecipe.Serializable> = REGISTER.registerType(HTConst.FORGING)

    //    Tank Interaction    //

    @JvmField
    val EMPTYING: HTDeferredRecipeType<HTTankEmptyingRecipe.Serializable> = REGISTER.registerType(HTConst.EMPTYING)

    @JvmField
    val FILLING: HTDeferredRecipeType<HTTankFillingRecipe.Serializable> = REGISTER.registerType(HTConst.FILLING)
}
