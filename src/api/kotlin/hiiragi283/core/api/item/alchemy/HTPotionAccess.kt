package hiiragi283.core.api.item.alchemy

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTFluidContent
import net.minecraft.core.component.DataComponentType

interface HTPotionAccess {
    companion object {
        @JvmField
        val INSTANCE: HTPotionAccess = HiiragiCoreAPI.getService()
    }

    val fluidContent: HTFluidContent
    val bottleTypeComponent: DataComponentType<HTBottleType>
}
