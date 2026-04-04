package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.impl.registry.HTDeferredMaterialRegister

data object HCMaterials {
    @JvmField
    val REGISTER = HTDeferredMaterialRegister(HiiragiCoreAPI.MOD_ID)

    //    Fuel    //

    //    Gem    //

    //    Metal    //
}
