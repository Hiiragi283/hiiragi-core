package hiiragi283.core.api.storage.amount

import java.util.function.IntConsumer
import java.util.function.LongConsumer

sealed interface HTAmountSetter<N> where N : Number, N : Comparable<N> {
    fun setAmount(amount: N)

    fun interface IntSized :
        HTAmountSetter<Int>,
        IntConsumer {
        override fun accept(value: Int) {
            setAmount(value)
        }
    }

    fun interface LongSized :
        HTAmountSetter<Long>,
        LongConsumer {
        override fun accept(value: Long) {
            setAmount(value)
        }
    }
}
