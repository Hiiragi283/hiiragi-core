package hiiragi283.core.common.storage.fluid

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.toResource
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.BiPredicate
import java.util.function.Predicate

open class HTBasicFluidTank protected constructor(
    private val capacity: Int,
    private val canExtract: BiPredicate<HTFluidResourceType, HTStorageAccess>,
    private val canInsert: BiPredicate<HTFluidResourceType, HTStorageAccess>,
    private val filter: Predicate<HTFluidResourceType>,
    private val listener: HTContentListener?,
) : HTFluidTank.Basic() {
    @JvmField
    protected var stack: FluidStack = FluidStack.EMPTY

    override fun setResource(resource: HTFluidResourceType?) {
        setResourceUnchecked(resource, true)
    }

    fun setResourceUnchecked(resource: HTFluidResourceType?, validate: Boolean = false) {
        if (resource == null) {
            if (this.getResource() == null) return
            this.stack = FluidStack.EMPTY
        } else if (!validate || isValid(resource)) {
            this.stack = resource.toStack(stack.amount)
        } else {
            error("Invalid stack for slot: $resource")
        }
        onContentsChanged()
    }

    override fun setAmount(amount: Int) {
        stack.amount = amount
    }

    final override fun isValid(resource: HTFluidResourceType): Boolean = this.filter.test(resource)

    final override fun isStackValidForInsert(resource: HTFluidResourceType, access: HTStorageAccess): Boolean =
        super.isStackValidForInsert(resource, access) && this.canInsert.test(resource, access)

    final override fun canStackExtract(resource: HTFluidResourceType, access: HTStorageAccess): Boolean =
        super.canStackExtract(resource, access) && this.canExtract.test(resource, access)

    override fun getResource(): HTFluidResourceType? = stack.toResource()

    override fun getCapacity(resource: HTFluidResourceType?): Int = capacity

    final override fun getAmount(): Int = stack.amount

    override fun serialize(output: HTValueOutput) {
        output.store(HTConst.FLUID, HTFluidResourceType.CODEC, getResource())
        output.putInt(HTConst.AMOUNT, stack.amount)
    }

    override fun deserialize(input: HTValueInput) {
        input.readAndSet(HTConst.FLUID, HTFluidResourceType.CODEC, ::setResource)
        input.getInt(HTConst.AMOUNT)?.let(::setAmount)
    }

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }
}
