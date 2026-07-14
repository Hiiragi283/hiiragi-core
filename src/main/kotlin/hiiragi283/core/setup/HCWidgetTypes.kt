package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.common.gui.widget.HTProgressWidget

data object HCWidgetTypes {
    @JvmField
    val FLUID: HTWidgetType.Simple<HTFluidWidget> = HTWidgetType.Simple(HiiragiCoreAPI.id(HTConst.FLUID))

    @JvmField
    val ITEM: HTWidgetType.Simple<HTItemWidget> = HTWidgetType.Simple(HiiragiCoreAPI.id(HTConst.ITEM))

    @JvmField
    val PROGRESS: HTWidgetType.Simple<HTProgressWidget> = HTWidgetType.Simple(HiiragiCoreAPI.id("progress"))
}
