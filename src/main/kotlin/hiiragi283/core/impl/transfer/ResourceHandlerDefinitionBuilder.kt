package hiiragi283.core.impl.transfer

import com.google.common.base.Predicates
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.transfer.HTHandlerAccess
import hiiragi283.core.api.transfer.ResourceHandlerDefinition
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.resource.Resource
import java.util.function.Predicate

class ResourceHandlerDefinitionBuilder<T : Resource>(size: Int, defaultCapacity: Int) {
    companion object {
        @JvmStatic
        inline fun <T : Resource> create(
            size: Int,
            defaultCapacity: Int,
            builderAction: ResourceHandlerDefinitionBuilder<T>.() -> Unit,
        ): ResourceHandlerDefinition<T> = ResourceHandlerDefinitionBuilder<T>(size, defaultCapacity).apply(builderAction).build()

        @JvmStatic
        inline fun createItem(
            size: Int,
            builderAction: ResourceHandlerDefinitionBuilder<ItemResource>.() -> Unit,
        ): ResourceHandlerDefinition<ItemResource> = create(size, HTConst.ABSOLUTE_MAX_STACK_SIZE, builderAction)

        @JvmStatic
        inline fun createFluid(
            size: Int,
            defaultCapacity: Int,
            builderAction: ResourceHandlerDefinitionBuilder<FluidResource>.() -> Unit,
        ): ResourceHandlerDefinition<FluidResource> = create(size, defaultCapacity, builderAction)
    }

    private val slots: Array<SlotBuilder<T>> = Array(size) { SlotBuilder(defaultCapacity) }

    operator fun set(index: Int, slotBuilder: SlotBuilder<T>) {
        slots[index] = slotBuilder
    }

    fun build(): ResourceHandlerDefinition<T> = object : ResourceHandlerDefinition<T> {
        override val size: Int = slots.size

        override fun getCapacity(index: Int, resource: T): Int = slots[index].capacity

        override fun isValid(index: Int, resource: T): Boolean = slots[index].isValid.test(resource)

        override fun canInsert(index: Int, access: HTHandlerAccess): Boolean = slots[index].canInsert.test(access)

        override fun canExtract(index: Int, access: HTHandlerAccess): Boolean = slots[index].canExtract.test(access)
    }

    //    SlotBuilder    //

    class SlotBuilder<T : Resource>(var capacity: Int) {
        var isValid: Predicate<T> = Predicates.alwaysTrue()
        var canInsert: Predicate<HTHandlerAccess> = Predicates.alwaysTrue()
        var canExtract: Predicate<HTHandlerAccess> = Predicates.alwaysTrue()
    }
}
