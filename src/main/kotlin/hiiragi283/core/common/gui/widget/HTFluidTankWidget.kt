package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.setup.HCWidgetTypes

class HTFluidTankWidget(private val tank: HTFluidTank) :
    HTWidget<HTFluidTankWidget>,
    HTFluidView by tank {
    override fun getType(): HTWidgetType<HTFluidTankWidget> = HCWidgetTypes.FLUID_TANK.get()

    override fun getBound(): HTBounds {
        TODO("Not yet implemented")
    }
}
