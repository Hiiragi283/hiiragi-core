package hiiragi283.core.api.gui.element

import com.lowdragmc.lowdraglib2.gui.ui.elements.FluidSlot
import com.lowdragmc.lowdraglib2.gui.ui.styletemplate.MCSprites
import com.lowdragmc.lowdraglib2.integration.xei.IngredientIO
import com.lowdragmc.lowdraglib2.integration.xei.emi.LDLibEMIPlugin
import com.lowdragmc.lowdraglib2.registry.annotation.LDLRegister
import dev.emi.emi.api.stack.EmiStackInteraction
import hiiragi283.core.api.integration.emi.slot.HTListFluidTank
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.api.storage.fluid.HTFluidHandler
import hiiragi283.core.api.storage.fluid.HTFluidTank
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Consumer

@LDLRegister(name = "hiiragi-fluid-slot", group = "inventory", registry = "ldlib2:ui_element")
class HTFluidSlotElement : FluidSlot {
    private val tank: HTFluidTank?

    constructor() : super() {
        this.tank = null
    }

    constructor(tank: HTFluidTank) : super() {
        this.tank = tank
    }

    constructor(handler: HTFluidHandler, index: Int) : super() {
        this.tank = handler.getFluidTank(index, null)
        bind(handler, index)
    }

    init {
        style.backgroundTexture(MCSprites.RECT_1)
        LDLibEMIPlugin.stackProvider(this) {
            if (tank is HTListFluidTank) {
                return@stackProvider EmiStackInteraction(tank.getIngredient())
            }
            // デフォルトの実装
            val stack: FluidStack = this.value
            if (stack.isEmpty) return@stackProvider null
            EmiStackInteraction(stack.toEmi(), null, false)
        }
    }

    override fun xeiRecipeIngredient(io: IngredientIO): HTFluidSlotElement {
        LDLibEMIPlugin.recipeIngredient(this, io) {
            when (tank) {
                is HTListFluidTank -> listOf(tank.getIngredient())
                else -> listOf(this.value.toEmi())
            }
        }
        return this
    }

    override fun xeiRecipeSlot(io: IngredientIO, chance: Float): HTFluidSlotElement {
        LDLibEMIPlugin.recipeSlot(this) {
            when (tank) {
                is HTListFluidTank -> tank.getIngredient()
                else -> this.value.toEmi()
            }
        }
        return this
    }

    override fun slotStyle(style: Consumer<SlotStyle>): HTFluidSlotElement {
        super.slotStyle(style)
        return this
    }

    override fun setFluid(fluid: FluidStack): HTFluidSlotElement {
        super.setFluid(fluid)
        return this
    }

    override fun setFluid(fluid: FluidStack, notify: Boolean): HTFluidSlotElement {
        super.setFluid(fluid, notify)
        return this
    }

    override fun setValue(value: FluidStack?): HTFluidSlotElement {
        super.setValue(value)
        return this
    }

    override fun setValue(value: FluidStack?, notify: Boolean): HTFluidSlotElement {
        super.setValue(value, notify)
        return this
    }
}
