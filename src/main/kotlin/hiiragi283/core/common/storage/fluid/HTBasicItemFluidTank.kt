package hiiragi283.core.common.storage.fluid

import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.impl.storage.fluid.HTMutableItemFluidTank
import hiiragi283.core.setup.HCDataComponents
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.SimpleFluidContent
import java.util.function.BiPredicate
import java.util.function.Predicate
import java.util.function.UnaryOperator

open class HTBasicItemFluidTank(
    private val containerUpdater: UnaryOperator<ItemStack>?,
    private val capacity: Int,
    private val canExtract: BiPredicate<HTFluidResourceType, HTStorageAccess>,
    private val canInsert: BiPredicate<HTFluidResourceType, HTStorageAccess>,
    private val filter: Predicate<HTFluidResourceType>,
    override var container: ItemStack,
) : HTMutableItemFluidTank() {
    companion object {
        @JvmStatic
        fun create(
            container: ItemStack,
            capacity: Int,
            canExtract: BiPredicate<HTFluidResourceType, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            canInsert: BiPredicate<HTFluidResourceType, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            filter: Predicate<HTFluidResourceType> = HTStoragePredicates.alwaysTrue(),
            containerUpdater: UnaryOperator<ItemStack>? = null,
        ): HTBasicItemFluidTank =
            HTBasicItemFluidTank(containerUpdater, HTBasicFluidTank.validateCapacity(capacity), canExtract, canInsert, filter, container)
    }

    protected fun getContent(): FluidStack = container.getOrDefault(HCDataComponents.FLUID, SimpleFluidContent.EMPTY).copy()

    protected fun updateContainer(resource: HTFluidResourceType?, amount: Int) {
        val content: SimpleFluidContent? = resource?.toStack(amount)?.let(SimpleFluidContent::copyOf)
        if (content == null || content.isEmpty) {
            container.remove(HCDataComponents.FLUID)
        } else {
            container.set(HCDataComponents.FLUID, content)
        }
        containerUpdater?.apply(container)?.let(::container::set)
    }

    //    HTMutableItemFluidTank    //

    override fun setResource(resource: HTFluidResourceType?) {
        updateContainer(resource, getAmount())
    }

    override fun setAmount(amount: Int) {
        updateContainer(getResource(), amount)
    }

    override fun getAmount(): Int = getContent().amount

    override fun getResource(): HTFluidResourceType? = getContent().toResource()

    override fun getCapacity(resource: HTFluidResourceType?): Int = capacity

    final override fun isValid(resource: HTFluidResourceType): Boolean = filter.test(resource)

    final override fun isStackValidForInsert(resource: HTFluidResourceType, access: HTStorageAccess): Boolean =
        super.isStackValidForInsert(resource, access) && canInsert.test(resource, access)

    final override fun canStackExtract(resource: HTFluidResourceType, access: HTStorageAccess): Boolean =
        super.canStackExtract(resource, access) && canExtract.test(resource, access)
}
