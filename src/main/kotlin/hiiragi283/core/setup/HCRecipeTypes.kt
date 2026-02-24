package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.recipe.HTItemToChancedRecipe
import hiiragi283.core.api.recipe.HTItemToItemRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister
import net.minecraft.world.item.crafting.SingleRecipeInput

object HCRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val CHARGING: HTDeferredRecipeType<SingleRecipeInput, HTItemToItemRecipe.Serializable> = REGISTER.registerType(HTConst.CHARGING)

    @JvmField
    val CRUSHING: HTDeferredRecipeType<SingleRecipeInput, HTItemToChancedRecipe.Serializable> = REGISTER.registerType(HTConst.CRUSHING)

    @JvmField
    val EXPLODING: HTDeferredRecipeType<HCExplodingRecipe.Input, HCExplodingRecipe> = REGISTER.registerType(HTConst.EXPLODING)
}
