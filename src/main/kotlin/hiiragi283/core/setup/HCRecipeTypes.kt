package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.recipe.HCAnvilCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCLightningChargingRecipe
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister
import net.minecraft.world.item.crafting.SingleRecipeInput

object HCRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val CHARGING: HTDeferredRecipeType<SingleRecipeInput, HCLightningChargingRecipe> = REGISTER.registerType(HTConst.CHARGING)

    @JvmField
    val CRUSHING: HTDeferredRecipeType<SingleRecipeInput, HCAnvilCrushingRecipe> = REGISTER.registerType(HTConst.CRUSHING)

    @JvmField
    val EXPLODING: HTDeferredRecipeType<SingleRecipeInput, HCExplodingRecipe> = REGISTER.registerType(HTConst.EXPLODING)
}
