package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.registry.HTDeferredWidgetTypeRegister
import hiiragi283.core.api.resource.SupplierWithId
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.common.gui.widget.HTProgressWidget

object HCWidgetTypes {
    @JvmField
    val REGISTER = HTDeferredWidgetTypeRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val FLUID: SupplierWithId<HTWidgetType<HTFluidWidget>> = REGISTER.registerType("fluid")

    @JvmField
    val ITEM: SupplierWithId<HTWidgetType<HTItemWidget>> = REGISTER.registerType("item")

    @JvmField
    val PROGRESS: SupplierWithId<HTWidgetType<HTProgressWidget>> = REGISTER.registerType("progress")
}
