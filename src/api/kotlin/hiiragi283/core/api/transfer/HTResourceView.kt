package hiiragi283.core.api.transfer

import com.google.common.primitives.Ints
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.resource.Resource

interface HTResourceView<T : Resource> {
    companion object {
        @JvmStatic
        fun <T : Resource> of(handler: ResourceHandler<T>, index: Int): HTResourceView<T> = object : HTResourceView<T> {
            override val resource: T get() = handler.getResource(index)

            override val amountAsLong: Long get() = handler.getAmountAsLong(index)

            override fun getCapacityAsLong(resource: T): Long = handler.getCapacityAsLong(index, resource)
        }
    }

    val resource: T

    val amountAsLong: Long
    val amountAsInt: Int get() = Ints.saturatedCast(amountAsLong)

    fun getCapacityAsLong(resource: T): Long

    fun getCapacityAsInt(resource: T): Int = Ints.saturatedCast(getCapacityAsLong(resource))
}

//    Extensions    //

fun <T : Resource> HTResourceView<T>.getNeededAsLong(resource: T): Long = maxOf(0, getCapacityAsLong(resource) - amountAsLong)

fun <T : Resource> HTResourceView<T>.getNeededAsInt(resource: T): Int = Ints.saturatedCast(this.getNeededAsLong(resource))
