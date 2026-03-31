package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCMeltingRecipe
import hiiragi283.core.impl.registry.HTDeferredRecipeType
import hiiragi283.core.impl.registry.HTDeferredRecipeTypeRegister
import net.minecraft.world.item.crafting.SingleRecipeInput

data object HCRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val CHARGING: HTDeferredRecipeType<SingleRecipeInput, HCChargingRecipe> = REGISTER.registerType(HTConst.CHARGING)

    @JvmField
    val MELTING: HTDeferredRecipeType<SingleRecipeInput, HCMeltingRecipe> = REGISTER.registerType(HTConst.MELTING)
}
