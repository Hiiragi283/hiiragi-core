package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.recipe.base.HTItemToItemRecipe
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.common.registry.HTDeferredRecipeTypeRegister
import net.minecraft.world.item.crafting.SingleRecipeInput

data object HCRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val CHARGING: HTDeferredRecipeType<SingleRecipeInput, HTItemToItemRecipe.Serializable> = REGISTER.registerType(HTConst.CHARGING)
}
