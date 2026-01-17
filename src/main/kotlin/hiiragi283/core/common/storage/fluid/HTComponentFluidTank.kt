package hiiragi283.core.common.storage.fluid

import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.attachments.HTAttachedFluids
import hiiragi283.core.api.storage.fluid.HTFluidResourceFactory
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.common.storage.HTCapabilityCodec
import hiiragi283.core.common.storage.component.HTComponentHandler
import hiiragi283.core.common.storage.component.HTComponentSlot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * @see mekanism.common.attachments.containers.fluid.ComponentBackedFluidTank
 */
class HTComponentFluidTank(
    attachedTo: ItemStack,
    size: Int,
    slot: Int,
    capacity: Int,
    canExtract: BiPredicate<HTFluidResourceType, HTStorageAccess>,
    canInsert: BiPredicate<HTFluidResourceType, HTStorageAccess>,
    filter: Predicate<HTFluidResourceType>,
) : HTComponentSlot<HTFluidResourceType, FluidStack, HTAttachedFluids>(attachedTo, size, slot, capacity, canExtract, canInsert, filter),
    HTFluidTank {
    companion object {
        @JvmStatic
        fun create(
            capacity: Int,
            context: HTComponentHandler.ContainerContext,
            canExtract: BiPredicate<HTFluidResourceType, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            canInsert: BiPredicate<HTFluidResourceType, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            filter: Predicate<HTFluidResourceType> = HTStoragePredicates.alwaysTrue(),
        ): HTComponentFluidTank =
            HTComponentFluidTank(context.attachedTo, context.size, context.index, capacity, canExtract, canInsert, filter)
    }

    override fun capabilityCodec(): HTCapabilityCodec<HTFluidTank, HTAttachedFluids> = HTCapabilityCodec.FLUID

    override fun resourceFactory(): HTFluidResourceFactory = HTFluidResourceFactory

    override fun getAmount(stack: FluidStack): Int = stack.amount
}
