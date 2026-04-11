package hiiragi283.core.common.storage.fluid

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.impl.storage.fluid.HTFluidStackResourceSlot
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * @see mekanism.common.capabilities.fluid.BasicFluidTank
 */
open class HTBasicFluidTank(
    private val capacity: Int,
    private val canExtract: BiPredicate<HTFluidResourceType, HTStorageAccess>,
    private val canInsert: BiPredicate<HTFluidResourceType, HTStorageAccess>,
    private val filter: Predicate<HTFluidResourceType>,
    private val listener: HTContentListener?,
) : HTFluidStackResourceSlot() {
    companion object {
        @JvmStatic
        fun validateCapacity(capacity: Int): Int {
            check(capacity >= 0) { "Capacity must be non negative" }
            return capacity
        }

        @JvmStatic
        fun create(
            listener: HTContentListener?,
            capacity: Int,
            canExtract: BiPredicate<HTFluidResourceType, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            canInsert: BiPredicate<HTFluidResourceType, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            filter: Predicate<HTFluidResourceType> = HTStoragePredicates.alwaysTrue(),
        ): HTBasicFluidTank = HTBasicFluidTank(validateCapacity(capacity), canExtract, canInsert, filter, listener)

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            capacity: Int,
            canInsert: Predicate<HTFluidResourceType> = HTStoragePredicates.alwaysTrue(),
            filter: Predicate<HTFluidResourceType> = canInsert,
        ): HTBasicFluidTank = create(
            listener,
            capacity,
            HTStoragePredicates.notExternal(),
            { resource: HTFluidResourceType, _ -> canInsert.test(resource) },
            filter,
        )

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: Int): HTBasicFluidTank =
            create(listener, capacity, canInsert = HTStoragePredicates.internalOnly())
    }

    @JvmField
    protected var stack: FluidStack = FluidStack.EMPTY

    final override fun getStackInternal(): FluidStack = stack.copy()

    override fun setStackInternal(stack: FluidStack) {
        setStackUnchecked(stack, false)
    }

    fun setStack(other: FluidStack) {
        setStackUnchecked(other, true)
    }

    private fun setStackUnchecked(other: FluidStack, validate: Boolean) {
        val resource: HTFluidResourceType? = getResourceFrom(other)
        if (resource == null) {
            if (this.stack.isEmpty) return
            this.stack = FluidStack.EMPTY
        } else if (!validate || isValid(resource)) {
            this.stack = other
        } else {
            error("Invalid stack for tank: $other")
        }
        onContentsChanged()
    }

    final override fun updateAmount(newAmount: Int) {
        stack.amount = newAmount
    }

    final override fun isValid(resource: HTFluidResourceType): Boolean = this.filter.test(resource)

    final override fun isStackValidForInsert(resource: HTFluidResourceType, access: HTStorageAccess): Boolean =
        super.isStackValidForInsert(resource, access) && this.canInsert.test(resource, access)

    final override fun canStackExtract(resource: HTFluidResourceType, access: HTStorageAccess): Boolean =
        super.canStackExtract(resource, access) && this.canExtract.test(resource, access)

    override fun getCapacity(resource: HTFluidResourceType?): Int = capacity

    override fun serialize(output: HTValueOutput) {
        output.write(HTConst.FLUID, FluidStack.OPTIONAL_CODEC, this.stack)
    }

    override fun deserialize(input: HTValueInput) {
        input.read(HTConst.FLUID, FluidStack.OPTIONAL_CODEC)?.let(::setStackInternal)
    }

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }
}
