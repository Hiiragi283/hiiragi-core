package hiiragi283.core.common.gui.sync

import hiiragi283.core.api.gui.sync.HTChangeType
import hiiragi283.core.api.gui.sync.HTSyncablePayload
import hiiragi283.core.api.transfer.HTSlotModifier
import hiiragi283.core.api.transfer.fluid.HTFluidView
import hiiragi283.core.api.transfer.fluid.stack
import hiiragi283.core.api.transfer.set
import hiiragi283.core.impl.transfer.fluid.HTBasicFluidTank
import net.minecraft.core.RegistryAccess
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

/**
 * [FluidStack]向けの[HTIntSyncSlot]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 * @see mekanism.common.inventory.container.sync.SyncableFluidStack
 */
class HTFluidSyncSlot(private val getter: Supplier<FluidStack>, private val setter: Consumer<FluidStack>) : HTIntSyncSlot {
    constructor(property: KMutableProperty0<FluidStack>) : this(property::get, property::set)

    constructor(slot: HTBasicFluidTank) : this(slot, slot)

    constructor(slot: HTFluidView, modifier: HTSlotModifier<FluidResource>) : this(slot::stack, modifier::set)

    private var lastStack: FluidStack = FluidStack.EMPTY

    var asFluidStack: FluidStack
        get() = this.getter.get()
        set(value) {
            this.setter.accept(value)
        }

    override var amountAsInt: Int
        get() = asFluidStack.amount
        set(value) {
            asFluidStack = asFluidStack.copyWithAmount(value)
        }

    override fun getChange(): HTChangeType? {
        val current: FluidStack = this.asFluidStack
        if (current.isEmpty && lastStack.isEmpty) {
            return null
        }
        val sameFluid: Boolean = FluidStack.isSameFluidSameComponents(current, lastStack)
        if (!sameFluid || this.amountAsInt != this.lastStack.amount) {
            this.lastStack = current.copy()
            return when {
                sameFluid -> HTChangeType.PARTIAL
                else -> HTChangeType.FULL
            }
        }
        return null
    }

    override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTSyncablePayload = when (changeType) {
        HTChangeType.PARTIAL -> HTIntSyncPayload(this.amountAsInt)
        HTChangeType.FULL -> HTFluidSyncPayload(this.asFluidStack.copy())
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): FluidStack = asFluidStack

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: FluidStack) {
        asFluidStack = value
    }
}
