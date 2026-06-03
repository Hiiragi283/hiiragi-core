package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.registry.HTDeferredMenuTypeRegister

data object HCMenuTypes {
    @JvmField
    val REGISTER = HTDeferredMenuTypeRegister(HiiragiCoreAPI.MOD_ID)
}
