package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.sync.HTSyncType
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.integration.jei.widget.HTGhostWidget
import hiiragi283.core.api.integration.jei.widget.HTIngredientWidget
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.common.gui.sync.HTFluidSyncSlot
import hiiragi283.core.impl.gui.widget.HTAbstractWidget
import hiiragi283.core.impl.storage.fluid.HTFluidStackResourceSlot
import hiiragi283.core.setup.HCWidgetTypes
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil

class HTFluidWidget private constructor(
    view: HTFluidView,
    private val fluidSlot: HTFluidSyncSlot,
    val isTank: Boolean,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : HTAbstractWidget(x - 1, y - 1, width, height),
    HTGhostWidget,
    HTIngredientWidget,
    HTFluidView by view {
    companion object {
        @JvmStatic
        fun createTank(tank: HTFluidStackResourceSlot, x: Int, y: Int): HTFluidWidget = createTank(tank, HTFluidSyncSlot(tank), x, y)

        @JvmStatic
        fun createTank(
            view: HTFluidView,
            fluidSlot: HTFluidSyncSlot,
            x: Int,
            y: Int,
        ): HTFluidWidget = HTFluidWidget(view, fluidSlot, true, x, y, 18, 18 * 3)

        @JvmStatic
        fun createSlot(tank: HTFluidStackResourceSlot, x: Int, y: Int): HTFluidWidget = createSlot(tank, HTFluidSyncSlot(tank), x, y)

        @JvmStatic
        fun createSlot(
            view: HTFluidView,
            fluidSlot: HTFluidSyncSlot,
            x: Int,
            y: Int,
        ): HTFluidWidget = HTFluidWidget(view, fluidSlot, false, x, y, 18, 18)
    }

    var backgroundType: HTBackgroundType = HTBackgroundType.NONE

    fun setBackground(background: HTBackgroundType): HTFluidWidget = apply {
        this.backgroundType = background
    }

    fun setStack(stack: FluidStack) {
        fluidSlot.asFluidStack = stack
    }

    override fun getType(): HTWidgetType<HTFluidWidget> = HCWidgetTypes.FLUID.get()

    override fun setupHolder(widgetHolder: HTWidgetHolder) {
        widgetHolder.track(
            fluidSlot,
            when (isGhost) {
                true -> HTSyncType.BOTH
                false -> HTSyncType.S2C
            },
        )
    }

    //    HTGhostWidget    //

    private var isGhost: Boolean = false

    fun setGhost(): HTFluidWidget = apply { this.isGhost = true }

    override fun getGhostConsumer(): HTGhostWidget.GhostIngredientConsumer? = when {
        !isGhost -> null
        else -> HTGhostWidget.FluidConsumer { stack: Any ->
            if (stack is FluidStack) {
                setStack(stack)
            }
        }
    }

    override fun mouseClicked(
        access: HTWidget.Access,
        mouseX: Double,
        mouseY: Double,
        button: Int,
    ) {
        if (isGhost) {
            FluidUtil
                .getFluidContained(access.carried)
                .ifPresentOrElse(
                    { stack: FluidStack -> fluidSlot.asFluidStack = stack.copy() },
                    { fluidSlot.asFluidStack = FluidStack.EMPTY },
                )
        }
    }

    //    HTIngredientWidget    //

    override fun getIngredient(): FluidStack = this.getFluidStack()
}
