package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.common.recipe.HTCrushingRecipe
import hiiragi283.core.common.recipe.HTDryingRecipe
import hiiragi283.core.common.recipe.HTExplodingRecipe
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister

object HCRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val CRUSHING: HTDeferredRecipeType<HTRecipeInput, HTCrushingRecipe> = REGISTER.registerType(HTConst.CRUSHING)

    @JvmField
    val DRYING: HTDeferredRecipeType<HTRecipeInput, HTDryingRecipe> = REGISTER.registerType(HTConst.DRYING)

    @JvmField
    val EXPLODING: HTDeferredRecipeType<HTRecipeInput, HTExplodingRecipe> = REGISTER.registerType(HTConst.EXPLODING)
}
