package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.sync.HTSyncType
import hiiragi283.core.api.gui.widget.HTAbstractWidget
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.integration.jei.widget.HTGhostWidget
import hiiragi283.core.api.integration.jei.widget.HTIngredientWidget
import hiiragi283.core.common.gui.sync.HTFluidSyncSlot
import hiiragi283.core.setup.HCWidgetTypes
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidUtil

class HTFluidWidget private constructor(
    private val fluidSlot: HTFluidSyncSlot,
    val isTank: Boolean,
    val backgroundType: HTBackgroundType,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : HTAbstractWidget(x - 1, y - 1, width, height),
    HTGhostWidget,
    HTIngredientWidget {
    companion object {
        @JvmStatic
        fun createTank(
            fluidSlot: HTFluidSyncSlot,
            x: Int,
            y: Int,
            backgroundType: HTBackgroundType,
        ): HTFluidWidget = HTFluidWidget(fluidSlot, true, backgroundType, x, y, 18, 18 * 3)

        @JvmStatic
        fun createSlot(
            fluidSlot: HTFluidSyncSlot,
            x: Int,
            y: Int,
            backgroundType: HTBackgroundType,
        ): HTFluidWidget = HTFluidWidget(fluidSlot, false, backgroundType, x, y, 18, 18)
    }

    fun getStack(): FluidStack = fluidSlot.asFluidStack

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
            setStack(FluidUtil.getFirstStackContained(access.carried).copy())
        }
    }

    //    HTIngredientWidget    //

    override fun getIngredient(): FluidStack = getStack()
}
