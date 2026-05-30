package hiiragi283.lib.gui.sync

import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.reflect.KMutableProperty0
import net.minecraft.core.RegistryAccess
import org.apache.commons.lang3.math.Fraction

/**
 * [Fraction]向けの[HTSyncableSlot]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
interface HTFractionSyncSlot : HTSyncableSlot {
    companion object {
        @JvmStatic
        fun create(array: Array<Fraction>, index: Int): HTFractionSyncSlot = create({ array[index] }, { array[index] = it })

        @JvmStatic
        fun create(property: KMutableProperty0<Fraction>): HTFractionSyncSlot = create(property::get, property::set)

        @JvmStatic
        fun create(getter: Supplier<Fraction>, setter: Consumer<Fraction>): HTFractionSyncSlot = Impl(getter, setter)
    }

    var amountAsFraction: Fraction

    private class Impl(private val getter: Supplier<Fraction>, private val setter: Consumer<Fraction>) : HTFractionSyncSlot {
        private var lastValue: Fraction = Fraction.ZERO

        override var amountAsFraction: Fraction
            get() = this.getter.get()
            set(value) {
                this.setter.accept(value)
            }

        override fun getChange(): HTChangeType? {
            val current: Fraction = this.amountAsFraction
            val last: Fraction = this.lastValue
            this.lastValue = current
            return when (current == last) {
                true -> null
                false -> HTChangeType.FULL
            }
        }

        override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTFractionSyncPayload = HTFractionSyncPayload(this.amountAsFraction)
    }
}
