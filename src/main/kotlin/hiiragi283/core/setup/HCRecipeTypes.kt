package hiiragi283.core.setup

import hiiragi283.core.api.HCConstants
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCChoppingRecipe
import hiiragi283.core.common.recipe.HCCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import hiiragi283.lib.recipe.HTRecipeType

data object HCRecipeTypes {
    //    In World    //

    @JvmField
    val CHARGING: HTRecipeType<HCChargingRecipe> = HTRecipeType(HiiragiCoreAPI.id(HCConstants.CHARGING))

    @JvmField
    val CHOPPING: HTRecipeType<HCChoppingRecipe> = HTRecipeType(HiiragiCoreAPI.id(HCConstants.CHOPPING))

    @JvmField
    val CRUSHING: HTRecipeType<HCCrushingRecipe> = HTRecipeType(HiiragiCoreAPI.id(HCConstants.CRUSHING))

    @JvmField
    val EXPLODING: HTRecipeType<HCExplodingRecipe> = HTRecipeType(HiiragiCoreAPI.id(HCConstants.EXPLODING))

    //    Tank Interaction    //

    @JvmField
    val EMPTYING: HTRecipeType<HCTankEmptyingRecipe> = HTRecipeType(HiiragiCoreAPI.id(HCConstants.EMPTYING))

    @JvmField
    val FILLING: HTRecipeType<HCTankFillingRecipe> = HTRecipeType(HiiragiCoreAPI.id(HCConstants.FILLING))

    @JvmField
    val ALL_TYPES: Set<HTRecipeType<*>> = setOf(
        CHARGING,
        CHOPPING,
        CRUSHING,
        EXPLODING,
        EMPTYING,
        FILLING,
    )
}
