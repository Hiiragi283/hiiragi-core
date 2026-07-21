package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.recipe.viewer.widget.HTGhostWidget
import hiiragi283.core.api.recipe.viewer.widget.HTIngredientWidget
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.setup.HCWidgetTypes
import hiiragi283.core.support.gui.widget.HTAbstractWidget
import hiiragi283.core.support.storage.fluid.HTFluidStackResourceSlot
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil

private typealias FluidStackSetter = (FluidStack) -> Unit

sealed class HTFluidWidget(view: HTFluidView, private val stackSetter: FluidStackSetter?, x: Int, y: Int, val backgroundType: HTBackgroundType, width: Int, height: Int, val isGhost: Boolean) :
    HTAbstractWidget(x - 1, y - 1, width, height),
    HTGhostWidget,
    HTIngredientWidget,
    HTFluidView by view {
    final override fun getType(): HTWidgetType<*> = HCWidgetTypes.FLUID

    final override fun mouseClicked(
        access: HTWidget.Access,
        mouseX: Double,
        mouseY: Double,
        button: Int,
    ) {
        if (isGhost) {
            stackSetter?.invoke(
                FluidUtil
                    .getFluidContained(access.carried)
                    .map(FluidStack::copy)
                    .orElseGet(FluidStack::EMPTY),
            )
        }
    }

    final override fun getIngredient(): FluidStack = this.getFluidStack()

    final override fun getGhostConsumer(): HTGhostWidget.GhostIngredientConsumer? = when {
        isGhost -> HTGhostWidget.FluidConsumer { stack ->
            if (stack is FluidStack) {
                stackSetter?.invoke(stack)
            }
        }
        else -> null
    }

    //    Slot    //

    class Slot(view: HTFluidView, stackSetter: FluidStackSetter?, x: Int, y: Int, backgroundType: HTBackgroundType, isGhost: Boolean) : HTFluidWidget(view, stackSetter, x, y, backgroundType, 18, 18, isGhost) {
        constructor(tank: HTFluidStackResourceSlot, x: Int, y: Int, backgroundType: HTBackgroundType, isGhost: Boolean) : this(tank, tank::setStack, x, y, backgroundType, isGhost)
    }

    //    Tank    //

    class Tank(view: HTFluidView, stackSetter: FluidStackSetter?, x: Int, y: Int, backgroundType: HTBackgroundType, isGhost: Boolean) : HTFluidWidget(view, stackSetter, x, y, backgroundType, 18, 18 * 3, isGhost) {
        constructor(tank: HTFluidStackResourceSlot, x: Int, y: Int, backgroundType: HTBackgroundType, isGhost: Boolean) : this(tank, tank::setStack, x, y, backgroundType, isGhost)
    }
}
