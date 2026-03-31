package hiiragi283.core.impl.transfer.fluid

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.transfer.HTHandlerAccess
import hiiragi283.core.api.transfer.HTTransferPredicates
import hiiragi283.core.api.transfer.fluid.HTFluidResourceConverter
import hiiragi283.core.impl.transfer.HTBasicResourceSlot
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource
import java.util.function.BiPredicate
import java.util.function.Predicate

open class HTBasicFluidTank(
    private val capacity: Int,
    canExtract: BiPredicate<FluidResource, HTHandlerAccess>,
    canInsert: BiPredicate<FluidResource, HTHandlerAccess>,
    filter: Predicate<FluidResource>,
    listener: HTContentListener?,
) : HTBasicResourceSlot.Stacked<FluidStack, FluidResource>(HTConst.FLUID, FluidStack.CODEC, canExtract, canInsert, filter, listener) {
    companion object {
        @JvmStatic
        private fun validateCapacity(capacity: Int): Int {
            check(capacity >= 0) { "Capacity must be non negative" }
            return capacity
        }

        @JvmStatic
        fun create(
            listener: HTContentListener?,
            capacity: Int,
            canExtract: BiPredicate<FluidResource, HTHandlerAccess> = HTTransferPredicates.alwaysTrueBi(),
            canInsert: BiPredicate<FluidResource, HTHandlerAccess> = HTTransferPredicates.alwaysTrueBi(),
            filter: Predicate<FluidResource> = HTTransferPredicates.alwaysTrue(),
        ): HTBasicFluidTank = HTBasicFluidTank(validateCapacity(capacity), canExtract, canInsert, filter, listener)

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            capacity: Int,
            canInsert: Predicate<FluidResource> = HTTransferPredicates.alwaysTrue(),
            filter: Predicate<FluidResource> = canInsert,
        ): HTBasicFluidTank = create(
            listener,
            capacity,
            HTTransferPredicates.notExternal(),
            { stack: FluidResource, _ -> canInsert.test(stack) },
            filter,
        )

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: Int): HTBasicFluidTank = create(
            listener,
            capacity,
            canInsert = HTTransferPredicates.internalOnly(),
        )
    }

    //    HTBasicResourceSlot    //

    override fun getConverter(): HTFluidResourceConverter = HTFluidResourceConverter

    override fun getCapacityAsLong(resource: FluidResource): Long = capacity.toLong()
}
