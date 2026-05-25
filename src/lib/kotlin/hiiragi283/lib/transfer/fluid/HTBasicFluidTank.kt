package hiiragi283.lib.transfer.fluid

import hiiragi283.lib.HTConstants
import hiiragi283.lib.serialization.readOption
import hiiragi283.lib.transfer.HTBasicResourceSlot
import hiiragi283.lib.transfer.HTHandlerAccess
import hiiragi283.lib.transfer.HTTransferPredicates
import java.util.function.BiPredicate
import java.util.function.Predicate
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.fluid.FluidResource

open class HTBasicFluidTank(capacity: Long, canInsert: BiPredicate<FluidResource, HTHandlerAccess>, canExtract: BiPredicate<FluidResource, HTHandlerAccess>, filter: Predicate<FluidResource>, listener: Runnable?) : HTBasicResourceSlot<FluidResource>(capacity, canInsert, canExtract, filter, listener, FluidResource.EMPTY) {
    companion object {
        @JvmStatic
        fun create(
            capacity: Long,
            listener: Runnable?,
            canInsert: BiPredicate<FluidResource, HTHandlerAccess> = HTTransferPredicates.alwaysTrueBi(),
            canExtract: BiPredicate<FluidResource, HTHandlerAccess> = HTTransferPredicates.alwaysTrueBi(),
            filter: Predicate<FluidResource> = HTTransferPredicates.alwaysTrue(),
        ): HTBasicFluidTank = HTBasicFluidTank(capacity, canInsert, canExtract, filter, listener)

        @JvmStatic
        fun input(
            capacity: Long,
            listener: Runnable?,
            canInsert: BiPredicate<FluidResource, HTHandlerAccess>,
            filter: Predicate<FluidResource> = HTTransferPredicates.alwaysTrue(),
        ): HTBasicFluidTank = create(capacity, listener, canInsert = canInsert, canExtract = HTTransferPredicates.notExternal(), filter = filter)

        @JvmStatic
        fun output(capacity: Long, listener: Runnable?): HTBasicFluidTank = create(capacity, listener, canInsert = HTTransferPredicates.internalOnly())
    }

    final override fun serialize(output: ValueOutput) {
        output.store(HTConstants.FLUID, FluidResource.CODEC, this.resourceIn)
        output.putLong(HTConstants.AMOUNT, this.amountIn)
    }

    final override fun deserialize(input: ValueInput) {
        input.readOption(HTConstants.FLUID, FluidResource.CODEC).onSome { resource: FluidResource ->
            val amount: Long = input.getLongOr(HTConstants.AMOUNT, 0)
            if (amount > 0) {
                this.resourceIn = resource
                this.amountIn = amount
            }
        }
    }
}
