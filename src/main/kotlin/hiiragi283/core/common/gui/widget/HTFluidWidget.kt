package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.sync.HTSyncType
import hiiragi283.core.api.gui.widget.HTAbstractWidget
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.integration.emi.widget.HTGhostWidget
import hiiragi283.core.api.integration.emi.widget.HTIngredientWidget
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.common.gui.sync.HTFluidSyncSlot
import hiiragi283.core.setup.HCWidgetTypes
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Consumer

abstract class HTFluidWidget(
    tank: HTFluidTank,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : HTAbstractWidget(x, y, width, height),
    HTIngredientWidget,
    HTFluidView by tank {
    var backgroundType: HTBackgroundType = HTBackgroundType.NONE

    override fun getIngredient(): FluidStack = this.getFluidStack()

    class TankWidget(tank: HTFluidTank, x: Int, y: Int) : HTFluidWidget(tank, x, y, 16, 18 * 3 - 2) {
        override fun getType(): HTWidgetType<*> = HCWidgetTypes.FLUID_TANK.get()
    }

    class StackWidget(
        tank: HTFluidTank,
        stackSetter: Consumer<FluidStack>?,
        x: Int,
        y: Int,
    ) : HTFluidWidget(tank, x, y, 18, 18),
        HTGhostWidget {
        constructor(tank: HTFluidTank, x: Int, y: Int) : this(tank, null, x, y)

        private val syncableSlot: HTFluidSyncSlot? = stackSetter?.let { HTFluidSyncSlot(tank::getFluidStack, it) }

        override fun getType(): HTWidgetType<*> = HCWidgetTypes.FLUID_STACK.get()

        override fun setupHolder(widgetHolder: HTWidgetHolder) {
            val slot: HTFluidSyncSlot = syncableSlot ?: return
            widgetHolder.track(slot, HTSyncType.BOTH)
        }

        override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
            syncableSlot?.asFluidStack = FluidStack.EMPTY
        }

        //    HTGhostWidget    //

        private val consumer = HTGhostWidget.FluidConsumer { stack: Any ->
            if (stack is FluidStack) {
                syncableSlot?.asFluidStack = stack
            }
        }

        override fun getGhostConsumer(): HTGhostWidget.FluidConsumer = consumer
    }
}
