package hiiragi283.core.api.item.alchemy

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTFluidContent

interface HTPotionAccess {
    companion object {
        @JvmField
        val INSTANCE: HTPotionAccess = HiiragiCoreAPI.getService()
    }

    val fluidContent: HTFluidContent
}
