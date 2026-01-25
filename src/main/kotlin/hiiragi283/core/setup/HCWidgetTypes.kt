package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.gui.widget.HTFluidTankWidget
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.registry.HTDeferredWidgetType
import hiiragi283.core.common.registry.register.HTDeferredWidgetTypeRegister

object HCWidgetTypes {
    @JvmField
    val REGISTER = HTDeferredWidgetTypeRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val FLUID_TANK: HTDeferredWidgetType<HTFluidTankWidget> = REGISTER.registerType("fluid_tank")

    @JvmField
    val ITEM_SLOT: HTDeferredWidgetType<HTItemSlotWidget> = REGISTER.registerType("item_slot")
}
