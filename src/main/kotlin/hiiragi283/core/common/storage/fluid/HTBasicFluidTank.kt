package hiiragi283.core.common.storage.fluid

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDataSerializable
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.toResource
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.BiConsumer
import java.util.function.BiPredicate
import java.util.function.Function
import java.util.function.Predicate

open class HTBasicFluidTank protected constructor(
    private val capacity: Int,
    private val canExtract: BiPredicate<HTFluidResourceType, HTStorageAccess>,
    private val canInsert: BiPredicate<HTFluidResourceType, HTStorageAccess>,
    private val filter: Predicate<HTFluidResourceType>,
) : HTFluidTank.Basic(),
    HTDataSerializable.CodecBased {
    companion object {
        @JvmStatic
        private fun validateCapacity(capacity: Int): Int {
            check(capacity >= 0) { "Capacity must be non negative" }
            return capacity
        }

        @JvmStatic
        fun create(
            capacity: Int,
            canExtract: BiPredicate<HTFluidResourceType, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            canInsert: BiPredicate<HTFluidResourceType, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            filter: Predicate<HTFluidResourceType> = HTStoragePredicates.alwaysTrue(),
        ): HTBasicFluidTank = HTBasicFluidTank(validateCapacity(capacity), canExtract, canInsert, filter)

        @JvmStatic
        fun input(
            capacity: Int,
            canInsert: Predicate<HTFluidResourceType> = HTStoragePredicates.alwaysTrue(),
            filter: Predicate<HTFluidResourceType> = canInsert,
        ): HTBasicFluidTank = create(
            capacity,
            HTStoragePredicates.notExternal(),
            { resource: HTFluidResourceType, _ -> canInsert.test(resource) },
            filter,
        )

        @JvmStatic
        fun output(capacity: Int): HTBasicFluidTank = create(capacity, canInsert = HTStoragePredicates.internalOnly())
    }

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
    }

    final override fun setAmount(amount: Int) {
        stack.amount = amount
    }

    final override fun isValid(resource: HTFluidResourceType): Boolean = this.filter.test(resource)

    final override fun isStackValidForInsert(resource: HTFluidResourceType, access: HTStorageAccess): Boolean =
        super.isStackValidForInsert(resource, access) && this.canInsert.test(resource, access)

    final override fun canStackExtract(resource: HTFluidResourceType, access: HTStorageAccess): Boolean =
        super.canStackExtract(resource, access) && this.canExtract.test(resource, access)

    override fun getResource(): HTFluidResourceType? = stack.toResource()

    override fun getCapacity(resource: HTFluidResourceType?): Int = capacity

    override fun getAmount(): Int = stack.amount

    override fun serialize(ops: RegistryOps<Tag>, consumer: BiConsumer<String, Tag>) {
        val resource: HTFluidResourceType = getResource() ?: return
        HTFluidResourceType.CODEC
            .encode(ops, resource)
            .ifSuccess { consumer.accept(HTConst.FLUID, it) }
        BiCodecs.NON_NEGATIVE_INT.encode(ops, getAmount()).ifSuccess { consumer.accept(HTConst.AMOUNT, it) }
    }

    override fun deserialize(ops: RegistryOps<Tag>, function: Function<String, Tag>) {
        HTFluidResourceType.CODEC
            .decode(ops, function.apply(HTConst.FLUID))
            .ifSuccess(::setResource)
        BiCodecs.NON_NEGATIVE_INT.decode(ops, function.apply(HTConst.AMOUNT)).ifSuccess(::setAmount)
    }
}
