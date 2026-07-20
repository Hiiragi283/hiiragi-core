package hiiragi283.core.common.gui.sync

import hiiragi283.core.api.gui.sync.HTChangeType
import hiiragi283.core.api.gui.sync.HTSyncableSlot
import hiiragi283.core.api.storage.amount.HTAmountView
import java.util.function.IntConsumer
import java.util.function.IntSupplier
import kotlin.reflect.KMutableProperty0
import net.minecraft.core.RegistryAccess

/**
 * [Int]向けの[HTSyncableSlot]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 * @see mekanism.common.inventory.container.sync.SyncableInt
 */
interface HTIntSyncSlot : HTSyncableSlot {
    companion object {
        @JvmStatic
        fun create(array: IntArray, index: Int): HTIntSyncSlot = create({ array[index] }, { array[index] = it })

        @JvmStatic
        fun create(property: KMutableProperty0<Int>): HTIntSyncSlot = create(property::get, property::set)

        /**
         * @since 0.9.0
         */
        @JvmStatic
        fun create(view: HTAmountView.Mutable): HTIntSyncSlot = create(view::getAmount, view::setAmount)

        @JvmStatic
        fun create(getter: IntSupplier, setter: IntConsumer): HTIntSyncSlot = Impl(getter, setter)
    }

    var amountAsInt: Int

    private class Impl(private val getter: IntSupplier, private val setter: IntConsumer) : HTIntSyncSlot {
        private var lastValue: Int = 0

        override var amountAsInt: Int
            get() = this.getter.asInt
            set(value) {
                this.setter.accept(value)
            }

        override fun getChange(): HTChangeType? {
            val current: Int = this.amountAsInt
            val last: Int = this.lastValue
            this.lastValue = current
            return when (current == last) {
                true -> null
                false -> HTChangeType.FULL
            }
        }

        override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTIntSyncPayload = HTIntSyncPayload(this.amountAsInt)
    }
}
