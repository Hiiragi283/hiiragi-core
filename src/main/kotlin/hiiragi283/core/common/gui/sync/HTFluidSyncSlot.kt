package hiiragi283.core.common.gui.sync

import hiiragi283.core.api.gui.sync.HTChangeType
import hiiragi283.core.api.gui.sync.HTSyncablePayload
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.getFluidStack
import net.minecraft.core.RegistryAccess
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.reflect.KMutableProperty0

/**
 * [FluidStack]向けの[HTIntSyncSlot]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 * @see mekanism.common.inventory.container.sync.SyncableFluidStack
 */
class HTFluidSyncSlot(private val getter: Supplier<FluidStack>, private val setter: Consumer<FluidStack>) : HTIntSyncSlot {
    constructor(property: KMutableProperty0<FluidStack>) : this(property::get, property::set)

    constructor(tank: HTFluidTank.Basic) : this(tank::getFluidStack, tank::setStack)

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
        val sameFluid: Boolean = current.fluid == this.lastStack.fluid
        if (!sameFluid || this.amountAsInt != this.lastStack.amount) {
            this.lastStack = current
            return when {
                sameFluid -> HTChangeType.PARTIAL
                else -> HTChangeType.FULL
            }
        }
        return null
    }

    override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTSyncablePayload? = when (changeType) {
        HTChangeType.PARTIAL -> HTIntSyncPayload(this.amountAsInt)
        HTChangeType.FULL -> HTFluidSyncPayload(this.asFluidStack)
    }
}
