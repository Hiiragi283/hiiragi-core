package hiiragi283.lib.transfer.fluid

import com.mojang.serialization.Codec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.serialization.readOption
import hiiragi283.lib.transfer.HTBasicResourceSlot
import hiiragi283.lib.transfer.HTHandlerAccess
import hiiragi283.lib.transfer.HTResourceStack
import hiiragi283.lib.transfer.HTTransferPredicates
import java.util.function.BiPredicate
import java.util.function.Predicate
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

open class HTBasicFluidTank(capacity: Long, canInsert: BiPredicate<FluidResource, HTHandlerAccess>, canExtract: BiPredicate<FluidResource, HTHandlerAccess>, filter: Predicate<FluidResource>, listener: Runnable?) : HTBasicResourceSlot<FluidResource>(capacity, canInsert, canExtract, filter, listener, FluidResource.EMPTY) {
    companion object {
        @JvmField
        val CODEC: Codec<HTResourceStack<FluidResource>> = HTResourceStack.codec(FluidResource.CODEC)

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
            canInsert: Predicate<FluidResource> = HTTransferPredicates.alwaysTrue(),
            filter: Predicate<FluidResource> = canInsert,
        ): HTBasicFluidTank = create(capacity, listener, canInsert = { resource, _ -> canInsert.test(resource) }, canExtract = HTTransferPredicates.notExternal(), filter = filter)

        @JvmStatic
        fun output(capacity: Long, listener: Runnable?): HTBasicFluidTank = create(capacity, listener, canInsert = HTTransferPredicates.internalOnly())
    }

    fun getStack(): FluidStack = this.createSnapshot().fold(FluidStack::EMPTY) { it.mapAsInt(FluidResource::toStack) }

    fun setStack(stack: FluidStack, transaction: TransactionContext?) {
        val (resource: FluidResource, amount: Int) = stack.toResourcePair()
        setContents(resource, amount.toLong(), transaction)
    }

    final override fun serialize(output: ValueOutput) {
        output.storeNullable(HTConstants.FLUID, CODEC, this.stackIn)
    }

    final override fun deserialize(input: ValueInput) {
        this.stackIn = input.readOption(HTConstants.FLUID, CODEC).getOrNull()
    }
}
