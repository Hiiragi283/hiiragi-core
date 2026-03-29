package hiiragi283.core.common.gui.sync

import hiiragi283.core.api.gui.sync.HTChangeType
import hiiragi283.core.api.gui.sync.HTSyncableSlot
import net.minecraft.core.RegistryAccess
import java.util.function.DoubleConsumer
import java.util.function.DoubleSupplier
import kotlin.reflect.KMutableProperty0

/**
 * [Double]向けの[HTSyncableSlot]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 * @see mekanism.common.inventory.container.sync.SyncableDouble
 */
interface HTDoubleSyncSlot : HTSyncableSlot {
    companion object {
        @JvmStatic
        fun create(array: DoubleArray, index: Int): HTDoubleSyncSlot = create({ array[index] }, { array[index] = it })

        @JvmStatic
        fun create(property: KMutableProperty0<Double>): HTDoubleSyncSlot = create(property::get, property::set)

        @JvmStatic
        fun create(getter: DoubleSupplier, setter: DoubleConsumer): HTDoubleSyncSlot = Impl(getter, setter)
    }

    var amountAsDouble: Double

    private class Impl(private val getter: DoubleSupplier, private val setter: DoubleConsumer) : HTDoubleSyncSlot {
        private var lastValue: Double = 0.0

        override var amountAsDouble: Double
            get() = this.getter.asDouble
            set(value) {
                this.setter.accept(value)
            }

        override fun getChange(): HTChangeType? {
            val current: Double = this.amountAsDouble
            val last: Double = this.lastValue
            this.lastValue = current
            return when (current == last) {
                true -> null
                false -> HTChangeType.FULL
            }
        }

        override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTDoubleSyncPayload =
            HTDoubleSyncPayload(this.amountAsDouble)
    }
}
