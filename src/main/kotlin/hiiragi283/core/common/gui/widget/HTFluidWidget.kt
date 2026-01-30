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
import hiiragi283.core.api.storage.fluid.HTMutableFluidTank
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.api.storage.fluid.setStack
import hiiragi283.core.common.gui.sync.HTFluidSyncSlot
import hiiragi283.core.setup.HCWidgetTypes
import net.minecraft.world.inventory.AbstractContainerMenu
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil
import java.util.function.Consumer

abstract class HTFluidWidget(
    tank: HTFluidTank,
    stackSetter: Consumer<FluidStack>?,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : HTAbstractWidget(x - 1, y - 1, width, height),
    HTIngredientWidget,
    HTFluidView by tank {
    var backgroundType: HTBackgroundType = HTBackgroundType.NONE

    fun setBackground(background: HTBackgroundType): HTFluidWidget = apply {
        this.backgroundType = background
    }

    protected val syncableSlot: HTFluidSyncSlot? = stackSetter?.let { HTFluidSyncSlot(tank::getFluidStack, it) }

    override fun setupHolder(widgetHolder: HTWidgetHolder) {
        val slot: HTFluidSyncSlot = syncableSlot ?: return
        widgetHolder.track(slot, HTSyncType.BOTH)
    }

    override fun getIngredient(): FluidStack = this.getFluidStack()

    //    TankWidget    //

    class TankWidget(
        tank: HTFluidTank,
        stackSetter: Consumer<FluidStack>?,
        x: Int,
        y: Int,
    ) : HTFluidWidget(tank, stackSetter, x, y, 18, 18 * 3) {
        constructor(tank: HTMutableFluidTank, x: Int, y: Int) : this(tank, tank::setStack, x, y)

        override fun getType(): HTWidgetType<*> = HCWidgetTypes.FLUID_TANK.get()
    }

    //    StackWidget    //

    class StackWidget(
        tank: HTFluidTank,
        stackSetter: Consumer<FluidStack>?,
        x: Int,
        y: Int,
    ) : HTFluidWidget(tank, stackSetter, x, y, 18, 18),
        HTGhostWidget {
        constructor(tank: HTMutableFluidTank, x: Int, y: Int) : this(tank, tank::setStack, x, y)

        override fun getType(): HTWidgetType<*> = HCWidgetTypes.FLUID_STACK.get()

        override fun mouseClicked(
            menu: AbstractContainerMenu,
            mouseX: Double,
            mouseY: Double,
            button: Int,
        ) {
            FluidUtil
                .getFluidContained(menu.carried)
                .ifPresentOrElse(
                    { stack ->
                        syncableSlot?.asFluidStack = stack.copy()
                    },
                    {
                        syncableSlot?.asFluidStack = FluidStack.EMPTY
                    },
                )
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
