package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.gui.widget.HTAbstractWidget
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.setup.HCWidgetTypes

class HTFluidTankWidget(private val tank: HTFluidTank, x: Int, y: Int) :
    HTAbstractWidget(x, y, 16, 18 * 3 - 2),
    HTFluidView by tank {
    override fun getType(): HTWidgetType<HTFluidTankWidget> = HCWidgetTypes.FLUID_TANK.get()
}
