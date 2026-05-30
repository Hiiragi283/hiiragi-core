package hiiragi283.lib.gui.sync

import hiiragi283.lib.transfer.fluid.FluidResourceHandler
import hiiragi283.lib.transfer.fluid.getFluidStack
import hiiragi283.lib.transfer.fluid.toResourcePair
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import net.minecraft.core.RegistryAccess
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.IndexModifier
import net.neoforged.neoforge.transfer.fluid.FluidResource

/**
 * [FluidStack]向けの[HTIntSyncSlot]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
class HTFluidSyncSlot(private val getter: Supplier<FluidStack>, private val setter: Consumer<FluidStack>) : HTIntSyncSlot {
    constructor(property: KMutableProperty0<FluidStack>) : this(property::get, property::set)

    constructor(handler: FluidResourceHandler, index: Int, modifier: IndexModifier<FluidResource>) : this(
        { handler.getFluidStack(index) },
        { stack: FluidStack ->
            val (resource: FluidResource, amount: Int) = stack.toResourcePair()
            modifier.set(index, resource, amount)
        },
    )

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
