package hiiragi283.core.common.gui.sync

import hiiragi283.core.api.gui.sync.HTChangeType
import hiiragi283.core.api.gui.sync.HTSyncableSlot
import it.unimi.dsi.fastutil.booleans.BooleanConsumer
import net.minecraft.core.RegistryAccess
import java.util.function.BooleanSupplier
import kotlin.reflect.KMutableProperty0

/**
 * [Boolean]向けの[HTSyncableSlot]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 * @see mekanism.common.inventory.container.sync.SyncableBoolean
 */
interface HTBoolSyncSlot : HTSyncableSlot {
    companion object {
        @JvmStatic
        fun create(array: BooleanArray, index: Int): HTBoolSyncSlot = create({ array[index] }, { array[index] = it })

        @JvmStatic
        fun create(property: KMutableProperty0<Boolean>): HTBoolSyncSlot = create(property::get, property::set)

        @JvmStatic
        fun create(getter: BooleanSupplier, setter: BooleanConsumer): HTBoolSyncSlot = Impl(getter, setter)
    }

    var asBool: Boolean

    private class Impl(private val getter: BooleanSupplier, private val setter: BooleanConsumer) : HTBoolSyncSlot {
        private var lastValue: Boolean = false

        override var asBool: Boolean
            get() = this.getter.asBoolean
            set(value) {
                this.setter.accept(value)
            }

        override fun getChange(): HTChangeType? {
            val current: Boolean = this.asBool
            val last: Boolean = this.lastValue
            this.lastValue = current
            return when (current == last) {
                true -> null
                false -> HTChangeType.FULL
            }
        }

        override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTBoolSyncPayload = HTBoolSyncPayload(this.asBool)
    }
}
