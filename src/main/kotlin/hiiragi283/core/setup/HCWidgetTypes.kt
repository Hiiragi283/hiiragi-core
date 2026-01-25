package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.common.registry.HTDeferredWidgetType
import hiiragi283.core.common.registry.register.HTDeferredWidgetTypeRegister

object HCWidgetTypes {
    @JvmField
    val REGISTER = HTDeferredWidgetTypeRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val FLUID_STACK: HTDeferredWidgetType<HTFluidWidget.StackWidget> = REGISTER.registerType("fluid_stack")

    @JvmField
    val FLUID_TANK: HTDeferredWidgetType<HTFluidWidget.TankWidget> = REGISTER.registerType("fluid_tank")

    @JvmField
    val ITEM_SLOT: HTDeferredWidgetType<HTItemWidget.SlotWidget> = REGISTER.registerType("item_slot")

    @JvmField
    val ITEM_STACK: HTDeferredWidgetType<HTItemWidget.StackWidget> = REGISTER.registerType("item_stack")
}
