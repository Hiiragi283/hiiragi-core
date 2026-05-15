package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.recipe.viewer.widget.HTGhostWidget
import hiiragi283.core.api.recipe.viewer.widget.HTIngredientWidget
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.common.gui.sync.HTFluidSyncSlot
import hiiragi283.core.impl.gui.widget.HTAbstractWidget
import hiiragi283.core.impl.storage.fluid.HTFluidStackResourceSlot
import hiiragi283.core.setup.HCWidgetTypes
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil

sealed class HTFluidWidget(
    view: HTFluidView,
    private val fluidSlot: HTFluidSyncSlot,
    x: Int,
    y: Int,
    val backgroundType: HTBackgroundType,
    width: Int,
    height: Int,
    val isGhost: Boolean,
) : HTAbstractWidget(x - 1, y - 1, width, height),
    HTGhostWidget,
    HTIngredientWidget,
    HTFluidView by view {
    fun getStack(): FluidStack = fluidSlot.asFluidStack

    fun setStack(stack: FluidStack) {
        fluidSlot.asFluidStack = stack
    }

    final override fun getType(): HTWidgetType<*> = HCWidgetTypes.FLUID.get()

    final override fun mouseClicked(
        access: HTWidget.Access,
        mouseX: Double,
        mouseY: Double,
        button: Int,
    ) {
        if (isGhost) {
            FluidUtil
                .getFluidContained(access.carried)
                .map(FluidStack::copy)
                .orElseGet(FluidStack::EMPTY)
                .let(::setStack)
        }
    }

    final override fun getIngredient(): FluidStack = getStack()

    final override fun getGhostConsumer(): HTGhostWidget.GhostIngredientConsumer? = when {
        isGhost -> HTGhostWidget.FluidConsumer { stack ->
            if (stack is FluidStack) {
                setStack(stack)
            }
        }
        else -> null
    }

    //    Slot    //

    class Slot(view: HTFluidView, fluidSlot: HTFluidSyncSlot, x: Int, y: Int, backgroundType: HTBackgroundType, isGhost: Boolean) : HTFluidWidget(view, fluidSlot, x, y, backgroundType, 18, 18, isGhost) {
        constructor(tank: HTFluidStackResourceSlot, x: Int, y: Int, backgroundType: HTBackgroundType, isGhost: Boolean) : this(tank, HTFluidSyncSlot(tank), x, y, backgroundType, isGhost)
    }

    //    Tank    //

    class Tank(view: HTFluidView, fluidSlot: HTFluidSyncSlot, x: Int, y: Int, backgroundType: HTBackgroundType, isGhost: Boolean) : HTFluidWidget(view, fluidSlot, x, y, backgroundType, 18, 18 * 3, isGhost) {
        constructor(tank: HTFluidStackResourceSlot, x: Int, y: Int, backgroundType: HTBackgroundType, isGhost: Boolean) : this(tank, HTFluidSyncSlot(tank), x, y, backgroundType, isGhost)
    }
}
