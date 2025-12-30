package hiiragi283.core.common.inventory.slot

import hiiragi283.core.api.inventory.slot.HTChangeType
import hiiragi283.core.api.inventory.slot.payload.HTSyncablePayload
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.common.inventory.slot.payload.HTFluidSyncPayload
import hiiragi283.core.common.inventory.slot.payload.HTIntSyncPayload
import net.minecraft.core.RegistryAccess
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Consumer
import java.util.function.Supplier

/**
 * [FluidStack]向けの[HTIntSyncSlot]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 * @see mekanism.common.inventory.container.sync.SyncableFluidStack
 */
class HTFluidSyncSlot(private val getter: Supplier<FluidStack>, private val setter: Consumer<FluidStack>) : HTIntSyncSlot {
    constructor(tank: HTFluidTank.Basic) : this(tank::getFluidStack, tank::setStack)

    private var lastStack: FluidStack = FluidStack.EMPTY

    fun getStack(): FluidStack = this.getter.get()

    fun setStack(stack: FluidStack) {
        this.setter.accept(stack)
    }

    override fun getAmountAsInt(): Int = getStack().amount

    override fun setAmountAsInt(amount: Int) {
        setStack(getStack().copyWithAmount(amount))
    }

    override fun getChange(): HTChangeType {
        val current: FluidStack = this.getStack()
        val sameFluid: Boolean = current.fluid == this.lastStack.fluid
        if (!sameFluid || this.getAmountAsInt() != this.lastStack.amount) {
            this.lastStack = current
            return when {
                sameFluid -> HTChangeType.AMOUNT
                else -> HTChangeType.FULL
            }
        }
        return HTChangeType.EMPTY
    }

    override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTSyncablePayload? = when (changeType) {
        HTChangeType.EMPTY -> null
        HTChangeType.AMOUNT -> HTIntSyncPayload(this.getAmountAsInt())
        HTChangeType.FULL -> HTFluidSyncPayload(this.getStack())
    }
}
