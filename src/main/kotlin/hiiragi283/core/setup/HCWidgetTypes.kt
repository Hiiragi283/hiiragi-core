package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.gui.widget.HTProgressWidget
import hiiragi283.core.common.registry.HTDeferredWidgetType
import hiiragi283.core.common.registry.register.HTDeferredWidgetTypeRegister

object HCWidgetTypes {
    @JvmField
    val REGISTER = HTDeferredWidgetTypeRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val FLUID: HTDeferredWidgetType<HTFluidWidget> = REGISTER.registerType("fluid")

    @JvmField
    val ITEM_SLOT: HTDeferredWidgetType<HTItemSlotWidget> = REGISTER.registerType("item_slot")

    @JvmField
    val PROGRESS: HTDeferredWidgetType<HTProgressWidget> = REGISTER.registerType("progress")
}
