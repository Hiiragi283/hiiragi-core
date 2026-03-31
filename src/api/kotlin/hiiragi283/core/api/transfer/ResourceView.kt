package hiiragi283.core.api.transfer

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.resource.Resource

interface ResourceView<T : Resource> {
    companion object {
        @JvmStatic
        fun <T : Resource> of(handler: ResourceHandler<T>, index: Int): ResourceView<T> = object : ResourceView<T> {
            override val index: Int = index
            override val resource: T get() = handler.getResource(index)

            override val amountAsLong: Long get() = handler.getAmountAsLong(index)
            override val amountAsInt: Int get() = handler.getAmountAsInt(index)

            override fun getCapacityAsLong(resource: T): Long = handler.getCapacityAsLong(index, resource)

            override fun getCapacityAsInt(resource: T): Int = handler.getCapacityAsInt(index, resource)
        }
    }

    val index: Int
    val resource: T

    val amountAsLong: Long
    val amountAsInt: Int

    fun getCapacityAsLong(resource: T): Long

    fun getCapacityAsInt(resource: T): Int
}

//    Extensions    //

val ResourceView<FluidResource>.stack: FluidStack get() = this.resource.toStack(this.amountAsInt)

val ResourceView<ItemResource>.stack: ItemStack get() = this.resource.toStack(this.amountAsInt)
