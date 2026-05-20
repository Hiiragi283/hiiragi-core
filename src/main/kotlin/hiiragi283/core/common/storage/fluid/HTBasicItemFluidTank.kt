package hiiragi283.core.common.storage.fluid

import hiiragi283.core.api.function.Identity
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.common.storage.HTStorageValidators
import hiiragi283.core.impl.storage.fluid.HTFluidStackResourceSlot
import hiiragi283.core.impl.storage.fluid.HTItemFluidTank
import hiiragi283.core.util.HTStorageHelper
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.BiPredicate
import java.util.function.Predicate

open class HTBasicItemFluidTank(
    private val containerUpdater: Identity<ItemStack>?,
    private val capacity: Int,
    private val canExtract: BiPredicate<HTFluidResourceType, HTStorageAccess>,
    private val canInsert: BiPredicate<HTFluidResourceType, HTStorageAccess>,
    private val filter: Predicate<HTFluidResourceType>,
    override var container: ItemStack,
) : HTFluidStackResourceSlot(),
    HTItemFluidTank {
    companion object {
        @JvmStatic
        fun create(
            container: ItemStack,
            capacity: Int,
            canExtract: BiPredicate<HTFluidResourceType, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            canInsert: BiPredicate<HTFluidResourceType, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            filter: Predicate<HTFluidResourceType> = HTStoragePredicates.alwaysTrue(),
            containerUpdater: Identity<ItemStack>? = null,
        ): HTBasicItemFluidTank = HTBasicItemFluidTank(containerUpdater, HTStorageValidators.validateCapacity(capacity), canExtract, canInsert, filter, container)
    }

    override fun getStack(): FluidStack = HTStorageHelper.getFluid(container)

    override fun setStack(stack: FluidStack) {
        setStackInternal(stack)
    }

    override fun setStackInternal(stack: FluidStack) {
        HTStorageHelper.updateFluid(container, stack)
        containerUpdater?.invoke(container)?.let(::container::set)
    }

    override fun updateAmount(newAmount: Int) {
        getStack().copyWithAmount(newAmount).let(::setStackInternal)
    }

    final override fun isValid(resource: HTFluidResourceType): Boolean = filter.test(resource)

    final override fun isStackValidForInsert(resource: HTFluidResourceType, access: HTStorageAccess): Boolean = super.isStackValidForInsert(resource, access) && canInsert.test(resource, access)

    final override fun canStackExtract(resource: HTFluidResourceType, access: HTStorageAccess): Boolean = super.canStackExtract(resource, access) && canExtract.test(resource, access)

    override fun getCapacity(resource: HTFluidResourceType?): Int = capacity
}
