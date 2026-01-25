package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTAbstractWidget
import hiiragi283.core.api.gui.widget.HTGhostWidget
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.common.gui.sync.HTFluidSyncSlot
import hiiragi283.core.setup.HCWidgetTypes
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Consumer

class HTFluidStackWidget(
    tank: HTFluidTank,
    stackSetter: Consumer<FluidStack>,
    x: Int,
    y: Int,
) : HTAbstractWidget(HTBounds.createSlot(x, y)),
    HTGhostWidget,
    HTFluidView by tank {
    private val syncableSlot = HTFluidSyncSlot(tank::getFluidStack, stackSetter)

    override fun getType(): HTWidgetType<*> = HCWidgetTypes.FLUID_STACK.get()

    override fun setupHolder(widgetHolder: HTWidgetHolder) {
        widgetHolder.track(syncableSlot)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        syncableSlot.asFluidStack = FluidStack.EMPTY
    }

    //    HTGhostWidget    //

    private val consumer = HTGhostWidget.FluidConsumer { stack: Any ->
        if (stack is FluidStack) {
            syncableSlot.asFluidStack = stack
        }
    }

    override fun getGhostConsumer(): HTGhostWidget.FluidConsumer = consumer
}
