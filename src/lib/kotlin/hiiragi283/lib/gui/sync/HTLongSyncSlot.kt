package hiiragi283.lib.gui.sync

import net.minecraft.core.RegistryAccess
import java.util.function.LongConsumer
import java.util.function.LongSupplier
import kotlin.reflect.KMutableProperty0

/**
 * [Long]向けの[HTSyncableSlot]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
interface HTLongSyncSlot : HTSyncableSlot {
    companion object {
        @JvmStatic
        fun create(array: LongArray, index: Int): HTLongSyncSlot = create({ array[index] }, { array[index] = it })

        @JvmStatic
        fun create(property: KMutableProperty0<Long>): HTLongSyncSlot = create(property::get, property::set)

        @JvmStatic
        fun create(getter: LongSupplier, setter: LongConsumer): HTLongSyncSlot = Impl(getter, setter)
    }

    var amountAsLong: Long

    private class Impl(private val getter: LongSupplier, private val setter: LongConsumer) : HTLongSyncSlot {
        private var lastValue: Long = 0

        override var amountAsLong: Long
            get() = this.getter.asLong
            set(value) {
                this.setter.accept(value)
            }

        override fun getChange(): HTChangeType? {
            val current: Long = this.amountAsLong
            val last: Long = this.lastValue
            this.lastValue = current
            return when (current == last) {
                true -> null
                false -> HTChangeType.FULL
            }
        }

        override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTLongSyncPayload = HTLongSyncPayload(this.amountAsLong)
    }
}
